package hydrograph.ui.graph.debug.service;

import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;



/**
 * The Class ViewDataServiceInitiator is used to start rest service in local mode @portNo#8004
 * @author vibhort
 *
 */
public class ViewDataServiceInitiator {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ViewDataServiceInitiator.class);
	public static void startService(){
		try{
			ServerSocket serverSocket = new ServerSocket(8004, 1, InetAddress.getLocalHost());
			if(!serverSocket.isClosed()){
				startServer();
			}

		}catch(BindException bindException){
			logger.error("Server is already started on port or is used by other process", bindException);
		} catch (InterruptedException interruptedException) {
			logger.error("Server process has been interrupted", interruptedException);
		} catch (UnknownHostException unknownHostException) {
			logger.error("Host is not known", unknownHostException);
		} catch (IOException ioException) {
			logger.error("Failure in IO", ioException);
		}
	}

	private static void startServer() throws InterruptedException, IOException {
		if(OSValidator.isWindows()){
			//"cmd", "/c", "start", "/b", "java","-jar", getInstallationPath()
			ProcessBuilder builder = new ProcessBuilder(new String[]{"cmd", "/c", "start", "/b", "java","-jar", getInstallationPath()});
			builder.start();
			//builder.redirectOutput(new File("C:/Users/vibhort/Desktop/Test_Vibhor"));
		}
		else if(OSValidator.isMac()){
			ProcessBuilder builder = new ProcessBuilder(new String[]{"java", "-jar", getInstallationPath()});
			builder.start();
			//builder.redirectOutput(new File("/Users/bitwise/Desktop/Test_Vibhor"));
			
		}
		else if(OSValidator.isUnix()){
			new ProcessBuilder(new String[]{"java", "-jar", getInstallationPath()}).start();
		}
		else if(OSValidator.isSolaris()){
			
		}
	}

	private static String getInstallationPath() {
		String path = Platform.getInstallLocation().getURL().getPath();
		if(StringUtils.isNotBlank(path) && StringUtils.startsWith(path, "/") && OSValidator.isWindows()){
			path = StringUtils.substring(path, 1);
		}
		return path + "config/service/elt-debug-0.1.9.jar";
	}
}