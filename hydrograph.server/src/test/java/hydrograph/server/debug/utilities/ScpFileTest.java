package hydrograph.server.debug.utilities;

import java.sql.Timestamp;
import java.util.Date;

public class ScpFileTest {
	public static void main(String[] args) {
		System.out.println("+++ Start: "
				+ new Timestamp((new Date()).getTime()));
		ScpFrom scpFrom = new ScpFrom();
		scpFrom.scpFileFromRemoteServer("10.130.248.53", "hduser",
				"Bitwise2012", "/tmp/generateRecord_input1_out0.csv",
				"C:\\Users\\Bhaveshs\\git\\elt-debug");
		System.out.println("+++ End: "
				+ new Timestamp((new Date()).getTime()));
	}
}
