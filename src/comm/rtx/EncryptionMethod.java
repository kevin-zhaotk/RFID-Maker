package comm.rtx;

import comm.rtx.RFIDDevice;

public class EncryptionMethod {

	public static EncryptionMethod mInstance = null;
	
	public static EncryptionMethod getInstance() {
		if (mInstance == null) {
			mInstance = new EncryptionMethod();
		}
		return mInstance;
	}
	public EncryptionMethod() {
		
	}
	
	/**
	 * 閫氳繃搴忓垪鍙疯幏鍙栧瘑閽
	 * @param sn
	 * @return
	 */
	public byte[] getKeyA(byte[] sn) {
		if (sn == null || sn.length != 4) {
			return null;
		}
		byte[] key = new byte[6];
		
		key[0] = (byte) (~(sn[0]) & 0x0ff );
		key[1] = (byte) (~(sn[1]) & 0x0ff );
		key[3] = (byte) (~(sn[2]) & 0x0ff );
		key[4] = (byte) (~(sn[3]) & 0x0ff );
		key[2] = (byte) ((key[0] ^ key[1] ^ key[3] ^ key[4]) & 0x0ff );
		// 璁＄畻鏍￠獙鍜�
		for (int i = 0; i < key.length-1; i++) {
			key[5] += (key[i] & 0x0ff);
		}
		byte tmp = 0;
		// 鏍￠獙鍜岃缁忚繃楂樹綆浣嶅�掑簭
		for (int i = 0; i < 8; i++) {
			if ((key[5] >> i & 0x01) == 0x01) {
				tmp |= 0x01 << (8-i);
			}
		}
		key[5] = (byte) (tmp & 0x0ff);
		return key;
	}
	
	/**
	 * 閫氳繃搴忓垪鍙疯幏鍙栧瘑閽
	 * @param sn
	 * @return
	 */
	public byte[] getKeyB(byte[] sn) {
		if (sn == null || sn.length == 0) {
			return null;
		}
		byte[] key = new byte[sn.length];
		for (int i = 0; i < sn.length; i++) {
			key[i] = (byte) ~(sn[i]);
		}
		for (int i = 0; i < key.length; i++) {
			key[i] = (byte) ((key[i]<<4 & 0x0f0) & (key[i] >> 4 & 0x0f));
		}
		return key;
	}
	
	
	/**
	 * 瑙ｅ瘑寰楀埌鐪熷疄鐨勫ⅷ姘村��
	 * 鏆傛椂灏嗗ⅷ姘村�兼斁鍦╞yte0锛堥珮锛夊拰byte1锛堜綆锛�
	 */
	public int decryptInkLevel(byte[] level) {
		int ink=0;
		if (level == null || level.length < 3) {
			return 0;
		}
		System.out.print("读出结果: ");
		for (int i = 0; i < 4; i++) {
			System.out.print(level[i] + ", ");
			//ink = (ink << (8*i)) | ((level[i]) & 0x0ff);
			ink = (int) (ink + (level[i]&0x0ff) * Math.pow(256,i));
		}
		System.out.println("ink level: " + ink);
		return ink;
		
	}
	
	public byte[] encryptInkLevel(int level) {
		if (level < RFIDDevice.INK_LEVEL_MIN || level > RFIDDevice.INK_LEVEL_MAX) {
			return null;
		}
		System.out.println("=======ink level========");
		byte[] ink = new byte[16];
		for (int i = 0; i < 4; i++) {
			ink[i] = (byte) ((level >> (8*i)) & 0x0ff);
			System.out.print(Integer.toHexString(ink[i]) + ", ");
		}
		for (int i = 4; i < ink.length; i++) {
			ink[i] = 0;
		}
		System.out.println("=======ink level========");
		return ink;
	}
}
