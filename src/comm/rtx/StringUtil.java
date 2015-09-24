package comm.rtx;

public class StringUtil {
	
	public static String byteArrayToString(byte[] data, int length) {

		String string = "";
		for (int i = 0; i < length; i++) {
			string += Integer.toHexString((0x0ff & data[i])) + " ";
		}
		return string;
	}
}
