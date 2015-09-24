package comm.rtx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FeaturesFile {

	private byte[] mFeatures; 
	public FeaturesFile() {
		setFile("features.txt");
	}
	
	public void setFile(String file) {
		String line;
		int index=0;
		mFeatures = new byte[16];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null) {
				mFeatures[index++] = (byte) (Integer.parseInt(line));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] getFeatures() {
		return mFeatures;
	}
	 
}
