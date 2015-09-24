package comm.rtx;

import java.nio.ByteBuffer;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;

public class RFIDData {
	
	/*璁剧疆妯″潡宸ヤ綔鍦� ISO14443 TYPE A 妯″紡*/
	public static final byte RFID_CMD_SETTYPE = 0x3A;
	public static final byte RFID_DATA_TYPEA = 0x41;
	/*Mifare one/Ultralight 鍗″鍗★細*/
	public static final byte RFID_CMD_SEARCH = 0x46;
	public static final byte RFID_DATA_CARDWAKEUP = 0x26;
	public static final byte RFID_DATA_CARDALL = 0x52;
	/*Ultralight 鍗￠�夊崱*/
	public static final byte RFID_CMD_SELECTCARD = 0x33;
	/*Ultralight 鍗¤鍗�*/
	public static final byte RFID_CMD_READCARD = 0x4b;
	/*Ultralight 鍗″啓鍗�*/
	public static final byte RFID_CMD_WRITECARD = 0x35;
	/*Mifare one 鍗￠槻鍐茬獊*/
	public static final byte RFID_CMD_AVOIDCONFLICT = 0x47;
	public static final byte RFID_DATA_AVOIDCONFLICT = 0x04;
	
	private static final byte mIdentificator = 0x10;
	private static final byte mHeader = 0x02;
	private static final byte mTailer = 0x03;
	/*宸茬粡鎻掑叆浜嗘暟鎹鲸璇嗙锛�0x10锛�*/
	public byte[] mTransData;
	/*杩囨护鎺夋暟鎹鲸璇嗙锛�0x10锛夌殑鐪熷疄鏁版嵁*/
	public byte[] mRealData;
	
	private int mDatalen;
	
	/*鏁版嵁鍖呭唴瀹�*/
	private byte mAddress[];
	private byte mLength;
	private byte mCommand;
	private byte[] mData;
	private byte mCheckCode;
	private byte mResult;
	
	/**
	 * 閫氳繃鍛戒护瀛楀拰鏁版嵁鍩熸瀯閫犳暟鎹�
	 * @param cmd 鍛戒护瀛�
	 * @param data 鏁版嵁鍩�
	 */
	public RFIDData(byte cmd, byte[] data) {
		mAddress = new byte[2];
		mAddress[0] = 0x00;
		mAddress[1] = 0x00;
		mLength = 0x00;
		mCheckCode = 0x00;
		mCommand = cmd;
		mData = data;
		//Debug.d("", "===>cmd:"+cmd +", mCommand:"+mCommand);
		
		ByteArrayBuffer buffer = new ByteArrayBuffer();
		//ByteArrayBuffer buffer = new ByteArrayBuffer(0);
		buffer.write(mHeader);
		buffer.write(mAddress[0]);
		buffer.write(mAddress[1]);
		buffer.write(mLength);
		buffer.write(mCommand);
		buffer.write(mData, 0, mData.length);
		buffer.write(mCheckCode);
		buffer.write(mTailer);
		mRealData = buffer.toByteArray();
//		for (int i=0; i<mRealData.length; i++) {
//			Debug.d("", "===>mRealData:"+mRealData[i]);
//			
//		}
		//璁＄畻闀垮害瀛�
		mLength = (byte) (mRealData.length-4);
		mRealData[3] = mLength;
		//璁＄畻鏍￠獙瀛�
		for (int i = 1; i < mRealData.length-2; i++) {
			mCheckCode += mRealData[i];	
			mRealData[mRealData.length-2] = mCheckCode;
		}
		getTransferData();
	}
	/**
	 * 鏋勯�燫FID鏁版嵁
	 * @param data
	 * @param isfilter 濡傛灉涓簍rue 琛ㄧずdata涓烘彃鍏ヤ簡鏁版嵁杈ㄨ瘑绗︾殑浼犺緭鏁版嵁锛屽鏋滀负false 琛ㄧずdata涓鸿繃婊ゆ帀鏁版嵁杈ㄨ瘑绗︾殑鐪熷疄鏁版嵁
	 */
	public RFIDData(byte[] data, boolean isfilter) {
		if (isfilter) {
			mTransData = data;
			getRealData();
			// Debug.d("", "---------11-------");
			// Debug.print(mRealData);
			// Debug.d("", "---------11-------");
			if (mRealData == null) {
				return ;
			}
			//璁＄畻闀垮害瀛�
			mLength = (byte) (mRealData.length-5);
			// Debug.d("", "-------len: "+mLength);
			// mRealData[3] = mLength;
			//璁＄畻鏍￠獙瀛�
			for (int i = 1; i < mRealData.length-2; i++) {
				mCheckCode += mRealData[i];	
			}
			// Debug.d("", "---------22-------");
			// Debug.print(mRealData);
			// Debug.d("", "---------22-------");
		} else {
			mRealData = data;
			getTransferData();
		}
	}
	
	/**
	 * 鏍规嵁鏈坊鍔犺鲸璇嗙鐨勬暟鎹緱鍒版坊鍔犺鲸璇嗙鐨勬暟鎹�
	 */
	private void getTransferData() {
		ByteArrayBuffer buffer = new ByteArrayBuffer();
		if( mRealData == null || mRealData.length <= 0) {
			return;
		}
		//娣诲姞甯уご
		buffer.write(mRealData[0]);
		//鏁版嵁鍖呭唴瀹�
		for (int i = 1; i < mRealData.length-1; i++) {
			if (mRealData[i] == mIdentificator || mRealData[i] == mHeader || mRealData[i] == mTailer) {
				// Debug.d("", "===>data:"+0x10);
				buffer.write((byte)0x10);
				// Debug.d("", "===>data:"+mRealData[i]);
				buffer.write(mRealData[i]);
			} else {
				buffer.write(mRealData[i]);
				// Debug.d("", "===>data:"+mRealData[i]);
			}
		}
		buffer.write(mRealData[mRealData.length-1]);
		//Debug.d("", "===>data:"+mRealData[mRealData.length-1]);
		mTransData = buffer.toByteArray();
		
	}
	
	/**
	 * 鏍规嵁娣诲姞杈ㄨ瘑绗︾殑鏁版嵁寰楀埌鏈坊鍔犺鲸璇嗙鐨勬暟鎹�
	 */
	private void getRealData() {
		ByteArrayBuffer buffer = new ByteArrayBuffer();
		if( mTransData == null || mTransData.length <= 0) {
			return;
		}
		//娣诲姞甯уご
		buffer.write(mTransData[0]);
		//鏁版嵁鍖呭唴瀹�
		for (int i = 1; i < mTransData.length-1; i++) {
			if (mTransData[i] == mIdentificator) {
				i++;
				buffer.write(mTransData[i]);
			} else {
				buffer.write(mTransData[i]);
			}
		}
		buffer.write(mTransData[mTransData.length-1]);
		
		mRealData = buffer.toByteArray();
	}
	
	public byte[] transferData() {
		return mTransData;
	}
	
	public int getLength() {
		return mDatalen;
	}
	
	public byte getCommand() {
		if (mRealData != null && mRealData.length > 4) {
			return mRealData[4];
		}
		return 0x00;
	}
	
	public byte[] getData() {
		
		if (mRealData == null || mRealData.length < 4) {
			return null;
		}
		/*鐢变簬 readblock杩斿洖鏁版嵁涓病鏈夐暱搴﹀瓧鑺傦紝鍥犳鏆傛椂涓嶈繘琛岄暱搴︽牎楠�
		mLength = mRealData[3];
		Debug.d("", "--->data length: "+mLength+",  real len: "+mRealData.length);
		if (mLength == 0 || (mLength + 5) != mRealData.length) {
			return null;
		}*/
		ByteBuffer buffer = ByteBuffer.wrap(mRealData);
		mData = new byte[mRealData.length - 7];
		if (mRealData[3] == 0x4B) {
			buffer.position(4);
		} else {
			buffer.position(5);
		}
		buffer.get(mData, 0, mRealData.length - 7);
		//Debug.d("", "-----------getData------");
		//Debug.print(mData);
		//Debug.d("", "-----------getData------");
		return mData;
	}
	
	
	@Override
	public String toString() {
		String data = "real: ";
		for (int i=0; i<mRealData.length; i++) {
			data += " " + String.format("0x%1$02x", mRealData[i]);
		}
		data += " , trans:";
		for (int i=0; i<mTransData.length; i++) {
			data += " " + String.format("0x%1$02x", mTransData[i]);
		}
		return data;
	}
}
