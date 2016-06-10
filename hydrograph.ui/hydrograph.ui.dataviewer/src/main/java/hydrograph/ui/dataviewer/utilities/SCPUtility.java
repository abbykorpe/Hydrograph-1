/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package hydrograph.ui.dataviewer.utilities;

import hydrograph.ui.logging.factory.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * 
 * Utility to scp files
 * 
 * @author Bitwise
 * 
 */
public class SCPUtility {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(SCPUtility.class);
	private static String SCP_ERROR="Unable to scp file";
	
	/**
	 * 
	 * Scp file from remote server
	 * 
	 * @param host
	 * @param user
	 * @param password
	 * @param remoteFile
	 * @param localFile
	 * @throws Exception
	 */
	public void scpFileFromRemoteServer(String host, String user, String password, String remoteFile, String localFile) throws Exception {
		FileOutputStream fos = null;
		try {

			String prefix = null;
			if (new File(localFile).isDirectory()) {
				prefix = localFile + File.separator;
			}

			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);

			// username and password will be given via UserInfo interface.
			UserInfo userInfo = new UserInformation(password);
			session.setUserInfo(userInfo);
			session.connect();

			// exec 'scp -f remoteFile' remotely
			String command = "scp -f " + remoteFile;
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] buf = new byte[1024];

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			fos = readRemoteFileAndWriteToLocalFile(localFile, fos, prefix, out, in, buf);
						
			session.disconnect();

		} catch (Exception e) {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw e;
		}
	}

	private FileOutputStream readRemoteFileAndWriteToLocalFile(String localFile, FileOutputStream fos, String prefix,
			OutputStream out, InputStream in, byte[] buf) throws IOException, FileNotFoundException {
		while (true) {
			int c = checkAck(in);
			if (c != 'C') {
				break;
			}

			// read '0644 '
			in.read(buf, 0, 5);

			long filesize = 0L;
			while (true) {
				if (in.read(buf, 0, 1) < 0) {
					// error
					break;
				}
				if (buf[0] == ' ')
					break;
				filesize = filesize * 10L + (long) (buf[0] - '0');
			}

			String file = null;
			for (int i = 0;; i++) {
				in.read(buf, i, 1);
				if (buf[i] == (byte) 0x0a) {
					file = new String(buf, 0, i);
					break;
				}
			}

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			// read a content of local file
			fos = new FileOutputStream(prefix == null ? localFile : prefix + file);
			int foo;
			while (true) {
				if (buf.length < filesize)
					foo = buf.length;
				else
					foo = (int) filesize;
				foo = in.read(buf, 0, foo);
				if (foo < 0) {
					// error
					break;
				}
				fos.write(buf, 0, foo);
				filesize -= foo;
				if (filesize == 0L)
					break;
			}
			fos.close();
			fos = null;

			if (checkAck(in) != 0) {
				//System.exit(0);
				return null;
			}

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
		}
		return fos;
	}

	private int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				logger.debug(sb.toString());
			}
			if (b == 2) { // fatal error
				logger.debug(sb.toString());
			}
		}
		return b;
	}

	/**
	 * 
	 * This class store user information who participates in scp
	 * 
	 * @author Bitwise
	 * 
	 */
	private static class UserInformation implements UserInfo {

		String password;

		public UserInformation(String password) {
			this.password = password;
		}

		@Override
		public String getPassphrase() {
			return password;
		}

		@Override
		public String getPassword() {
			return this.password;
		}

		@Override
		public boolean promptPassphrase(String arg0) {
			return true;
		}

		@Override
		public boolean promptPassword(String arg0) {
			return true;
		}

		@Override
		public boolean promptYesNo(String arg0) {
			return true;
		}

		@Override
		public void showMessage(String arg0) {

		}

	}

}
