package comm.rtx;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.IllegalFormatCodePointException;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;

public class RFIDDevice {

	//RFID���� native�ӿ�
	public static native int open(String dev);
	public static native int close(int fd);
	public static native int write(int fd, short[] buf, int len);
	public static native byte[] read(int fd, int len);
	

	/************************
	 * RFID�����������
	 ***********************/
	public static final String TAG = RFIDDevice.class.getSimpleName();
	
	public static RFIDDevice mRfidDevice;
	//���ڽڵ�
	public static final String SERIAL_INTERFACE = "/dev/ttyS3";
	
	/*īˮ��������*/
	public static final int INK_LEVEL_MAX = 10000000;
	public static final int INK_LEVEL_MIN = 0;
	
	//block definition
	public static byte SECTOR_FEATURE = 0x04;
	public static byte BLOCK_FEATURE = 0x01;
	public static byte BLOCK_KEY = 0x03;
	
	public static byte SECTOR_COPY_FEATURE = 0x05;
	public static byte BLOCK_COPY_FEATURE = 0x01;
	
	public static byte SECTOR_INKLEVEL = 0x04;
	public static byte BLOCK_INKLEVEL = 0x02;
	public static byte SECTOR_COPY_INKLEVEL = 0x05;
	public static byte BLOCK_COPY_INKLEVEL = 0x02;
	
	public static byte SECTOR_INK_MAX = 0x04;
	public static byte BLOCK_INK_MAX = 0x00;
	
	
	//Command
	public static byte RFID_CMD_CONNECT = 0x15;
	public static byte RFID_CMD_TYPEA = 0x3A;
	public static byte RFID_CMD_SEARCHCARD = 0x46;
	public static byte RFID_CMD_MIFARE_CONFLICT_PREVENTION = 0x47;
	public static byte RFID_CMD_MIFARE_CARD_SELECT = 0x48;
	public static byte RFID_CMD_MIFARE_KEY_VERIFICATION = 0x4A;
	public static byte RFID_CMD_MIFARE_READ_BLOCK = 0x4B;
	public static byte RFID_CMD_MIFARE_WRITE_BLOCK = 0x4C;
	public static byte RFID_CMD_MIFARE_WALLET_INIT = 0x4D;
	public static byte RFID_CMD_MIFARE_WALLET_READ = 0x4E;
	public static byte RFID_CMD_MIFARE_WALLET_CHARGE = 0x50;
	public static byte RFID_CMD_MIFARE_WALLET_DEBIT = 0x4F;
	
	//Data
	public static byte[] RFID_DATA_CONNECT = {0x03};
	public static byte[] RFID_DATA_TYPEA = {0x41};
	public static byte[] RFID_DATA_SEARCHCARD_WAKE = {0x26};
	public static byte[] RFID_DATA_SEARCHCARD_ALL = {0x52};
	public static byte[] RFID_DATA_MIFARE_CONFLICT_PREVENTION = {0x04};
	public static byte[] RFID_DATA_MIFARE_KEY_A = {0x60,0x00, 0x00,0x00,0x00,0x00,0x00,0x00};
	public static byte[] RFID_DATA_MIFARE_KEY_B = {0x61,0x00, 0x00,0x00,0x00,0x00,0x00,0x00};
	//����ֵ
	public static byte	RFID_RESULT_OK = 0x00;
	public static byte[] RFID_RESULT_MIFARE_S50 = {0X04, 0X00};
	public static byte[] RFID_RESULT_MIFARE_S70 = {0X02, 0X00};
	public static byte[] RFID_RESULT_UTRALIGHT = {0X44, 0X00};
	
	//Ĭ����Կ
	public static byte[] RFID_DEFAULT_KEY_A = { (byte) 0x0ff, (byte) 0x0ff, (byte) 0x0ff, (byte) 0x0ff, (byte) 0x0ff, (byte) 0x0ff};
	public static byte[] RFID_DEFAULT_KEY_B = { (byte) 0x0ff, (byte) 0x0ff, (byte) 0x0ff, (byte) 0x0ff, (byte) 0x0ff, (byte) 0x0FF};
	
	//����õ�����Կ
	public byte[] mRFIDKeyA = null;
	public byte[] mRFIDKeyB = null;
	
	
	// �����붨��
	public static final int RFID_ERRNO_NOERROR = 0;
	public static final int RFID_ERRNO_NOCARD = 1;
	public static final int RFID_ERRNO_SERIALNO_UNAVILABLE = 2;
	public static final int RFID_ERRNO_SELECT_FAIL = 3;
	public static final int RFID_ERRNO_KEYVERIFICATION_FAIL = 4;
	
	//����
	public static int mFd=0;
	
	public OutputStream mStream;
	
	public static RFIDDevice getInstance() {
		if (mRfidDevice == null) {
			mRfidDevice = new RFIDDevice();
		}
		
		return mRfidDevice;
	}
	
	public void setOutputStream(OutputStream oStream) {
		mStream = oStream;
	}
	/*
	 * �˿�����
	 */
	public boolean connect() {
		RFIDData data = new RFIDData(RFID_CMD_CONNECT, RFID_DATA_CONNECT);
		byte[] readin = writeCmd(data);
		return isCorrect(readin);
			
	}
	/*
	 * ���ö���������ģʽ
	 */
	public boolean setType() {
		RFIDData data = new RFIDData(RFID_CMD_TYPEA, RFID_DATA_TYPEA);
		byte[] readin = writeCmd(data);
		return isCorrect(readin);
	}
	/*
	 * Ѱ��
	 */
	public boolean lookForCards() {
		RFIDData data = new RFIDData(RFID_CMD_SEARCHCARD, RFID_DATA_SEARCHCARD_ALL);
		byte[] readin = writeCmd(data);
		if (readin == null) {
			return false;
		}
		RFIDData rfidData = new RFIDData(readin, true);
		byte[] rfid = rfidData.getData();
		if (rfid == null || rfid[0] != 0 || rfid.length < 3) {
			System.out.println( "===>rfid data error");
			return false;
		}
		if (rfid[1] == 0x04 && rfid[2] == 0x00) {
			System.out.println( "===>rfid type S50");
			return true;
		} else if (rfid[1] == 0x02 && rfid[2] == 0x00) {
			System.out.println( "===>rfid type S70");
			return true;
		} else if (rfid[1] == 0x44 && rfid[2] == 0x00) {
			System.out.println( "===>rfid type utralight");
			return true;
		} else {
			System.out.println( "===>unknow rfid type");
			return false;
		}
	}
	/*
	 * ����ͻ
	 */
	public byte[] avoidConflict() {
		RFIDData data = new RFIDData(RFID_CMD_MIFARE_CONFLICT_PREVENTION, RFID_DATA_MIFARE_CONFLICT_PREVENTION);
		byte[] readin = writeCmd(data);
		RFIDData rfidData = new RFIDData(readin, true);
		byte[] rfid = rfidData.getData();
		System.out.println(StringUtil.byteArrayToString(rfid, rfid.length));
		if (rfid == null || rfid[0] != 0 || rfid.length != 5) {
			System.out.println( "===>rfid data error");
			return null;
		}
		ByteBuffer buffer = ByteBuffer.wrap(rfid);
		buffer.position(1);
		byte[] serialNo = new byte[4]; 
		buffer.get(serialNo, 0, serialNo.length);
		System.out.println("========sn=====");
		System.out.println(StringUtil.byteArrayToString(serialNo, serialNo.length));
		System.out.println("========sn=====");
		return serialNo;
	}
	/*
	 * ѡ��
	 */ 
	public boolean selectCard(byte[] cardNo) {
		if (cardNo == null || cardNo.length != 4) {
			System.out.println("===>select card No is null");
			return false;
		}
		RFIDData data = new RFIDData(RFID_CMD_MIFARE_CARD_SELECT, cardNo);
		byte[] readin = writeCmd(data);
		return isCorrect(readin);
	}
	
	/**
	 * ��Կ��֤
	 * @param data
	 * @return
	 */
	public boolean keyVerfication(byte sector, byte block, byte[] key) {
		
		if (sector >= 16 || block >= 4) {
			System.out.println( "===>block over");
			return false;
		}
		byte blk = (byte) (sector*4 + block); 
		if (key == null || key.length != 6) {
			System.out.println( "===>invalide key");
			return false;
		}
		System.out.println( "===>keyVerfication sector:" + sector + ", block:" +block);
		byte[] keyA = {0x60,blk, key[0], key[1], key[2], key[3], key[4], key[5]};
		RFIDData data = new RFIDData(RFID_CMD_MIFARE_KEY_VERIFICATION, keyA);
		byte[] readin = writeCmd(data);
		return isCorrect(readin);
	}
	
	/**
	 * Mifare one ��д��
	 * @param block 1�ֽھ��Կ��
	 * @param content 16�ֽ�����
	 * @return true �ɹ��� false ʧ��
	 */
	public boolean writeBlock(byte sector, byte block, byte[] content) {
		
		if (sector >= 16 || block >= 4) {
			System.out.println( "===>block over");
			return false;
		}
		byte blk = (byte) (sector*4 + block); 
		if (content == null || content.length != 16) {
			System.out.println( "block no large than 0x3f");
		}
		ByteArrayBuffer buffer = new ByteArrayBuffer(0);
		buffer.write(blk);
		buffer.write(content, 0, content.length);
		RFIDData data = new RFIDData(RFID_CMD_MIFARE_WRITE_BLOCK, buffer.toByteArray());
		byte[] readin = writeCmd(data);
		return isCorrect(readin);
		
	}
	
	/**
	 * Mifare one ������
	 * @param sector 1�ֽ�����
	 * @param block 1�ֽ���Կ��
	 * @return 16�ֽ�����
	 */
	public byte[] readBlock(byte sector, byte block) {
		if (sector >= 16 || block >= 4) {
			System.out.println( "===>block over");
			return null;
		}
		System.out.println( "===>readBlock sector:" + sector + ", block:" +block);
		byte blk = (byte) (sector*4 + block); 
		byte[] b = {blk};
		RFIDData data = new RFIDData(RFID_CMD_MIFARE_READ_BLOCK, b);
		byte[] readin = writeCmd(data);
		if (!isCorrect(readin)) {
			return null;
		}
		RFIDData rfidData = new RFIDData(readin, true);
		byte[] blockData = rfidData.getData();
		return blockData;
	}
	/*
	 * write command to RFID model
	 */
	private byte[] writeCmd(RFIDData data) {
		
		System.out.println( "************write begin******************");
		//Debug.print(data.mTransData);
		System.out.println( "************write end******************");
		try {
			mStream.write(data.transferData());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private boolean isCorrect(byte[] value) {
		
		if (value == null || value.length == 0) {
			return false;
		}
		RFIDData rfidData = new RFIDData(value, true);
		byte[] rfid = rfidData.getData();
		if (rfid == null || rfid[0] != 0) {
			System.out.println( "===>rfid data error");
			return false;
		}
		return true;
	}
	
	public class RFIDCardType {
		public static final int TYPE_S50 = 0;
		public static final int TYPE_S70 = 1;
		public static final int TYPE_UTRALIGHT = 2;
	}
	
	
	private int cardInit() {
		//Ѱ��
		if (!lookForCards()) {
			return RFID_ERRNO_NOCARD;
		}
		//����ͻ
		byte[] sn = avoidConflict();
		if (sn == null || sn.length == 0) {
			return RFID_ERRNO_SERIALNO_UNAVILABLE;
		}
		System.out.println( "+++++++++++ SN +++++++++++");
		System.out.println(StringUtil.byteArrayToString(sn, sn.length));
		System.out.println( "+++++++++++ SN +++++++++++");
		//ѡ��
		if (!selectCard(sn)) {
			return RFID_ERRNO_SELECT_FAIL;
		}
		return RFID_ERRNO_NOERROR;
	}
	/**
	 * read serial No. using default key
	 * @return 4bytes serial No.
	 */
	public byte[] getSerialNo() {
		if (!keyVerfication((byte) 0, (byte) 0, RFID_DEFAULT_KEY_A)) {
			return null;
		}
		
		byte[] block = readBlock((byte)0, (byte) 0);
		System.out.println( "*********block 0*********");
		System.out.println(StringUtil.byteArrayToString(block, block.length));
		System.out.println( "*********block 0*********");
		byte[] sn = new byte[4];
		ByteArrayInputStream stream = new ByteArrayInputStream(block);
		stream.skip(1);
		stream.read(sn, 0, 4);
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sn;
	}
	
	/**
	 * ��ʼ�����̣�1��Ѱ���� 2������ͻ�� 3��ѡ��
	 * ʹ��Ĭ����Կ��ȡuid ��block1������
	 * ͨ����Ӧ�㷨�õ�A��B��Կ
	 * @return ѡ���ɹ�����true��ʧ�ܷ���false
	 */
	public synchronized int init() {
		int errno = 0;
		if (( errno = cardInit()) != RFID_ERRNO_NOERROR) {
			return errno;
		}
		// ��UID
		byte[] uid = getSerialNo();
		if (uid == null || uid.length != 4) {
			System.out.println( "===>get uid fail");
		}
		System.out.println(StringUtil.byteArrayToString(uid, uid.length));
		EncryptionMethod method = EncryptionMethod.getInstance();
		byte [] key = method.getKeyA(uid);
		setKeyA(key);
		System.out.println(StringUtil.byteArrayToString(key, key.length));
		
		return 0;
	}
	
	
	/**
	 * ����ֵ��ȡ������ֵ������sector 4�� �� sector5  �� block 2.
	 * ����ֵ����˫�����ݣ������Ҫ�Զ�ȡ�����ݽ���У��
	 */
	private int getInkLevel(boolean isBackup) {
		byte sector = 0;
		byte block = 0;
		if (isBackup) {
			sector = SECTOR_INKLEVEL;
			block = BLOCK_INKLEVEL;
		} else {
			sector = SECTOR_COPY_INKLEVEL;
			block = BLOCK_COPY_INKLEVEL;
		}
		System.out.println( "===>getLevel sector:" + sector + ", block:" + block);
		if ( !keyVerfication(sector, block, RFID_DEFAULT_KEY_A))
		{
			return 0;
		}
		byte[] ink = readBlock(sector, block);
		System.out.println( "************ink level***********");
		System.out.println(StringUtil.byteArrayToString(ink, ink.length));
		System.out.println( "************ink level***********"); 
		EncryptionMethod encryt = EncryptionMethod.getInstance();
		return encryt.decryptInkLevel(ink);
	}
	
	public int getInkLevel() {
		System.out.println( "===>getLevel");
		// �ȴ���block��ȡīˮֵ
		int current = getInkLevel(false);
		if (!isLevelValid(current)) {
			// �����blockīˮֵ���Ϸ���ӱ�������ȡ
			current = getInkLevel(true);
		}
		if (!isLevelValid(current)) {
			return 0;
		}
		if (current == 0) {
			return 0;
		}
		//current = current/Configs.INK_LEVEL_MAX > 1 ? current/Configs.INK_LEVEL_MAX : 1;
		return current;
	}
	/**
	 * ����ֵд��
	 */
	private void setInkLevel(int level, boolean isBack) {
		byte sector = 0;
		byte block = 0;
		if (isBack) {
			sector = SECTOR_INKLEVEL;
			block = BLOCK_INKLEVEL;
		} else {
			sector = SECTOR_COPY_INKLEVEL;
			block = BLOCK_COPY_INKLEVEL;
		}
		
		if ( !keyVerfication(sector, block, RFID_DEFAULT_KEY_A))
		{
			return ;
		}
		System.out.println( "===>setInkLevel sector:" + sector + ", block:" + block);
		EncryptionMethod encryte = EncryptionMethod.getInstance();
		byte[] content = encryte.encryptInkLevel(level);
		if (content == null) {
			return ;
		}
		writeBlock(sector, block, content);
	}

	/**
	 *����īˮֵ������ǰīˮֵ��1 
	 */
	public int updateInkLevel() {
		System.out.println( "===>updateInkLevel");
		int level = getInkLevel(false);
		if (!isLevelValid(level)) {
			// �����blockīˮֵ���Ϸ���ӱ�������ȡ
			level = getInkLevel(true);
		}
		if (!isLevelValid(level)) {
			return 0;
		}
		System.out.println( "===>updateInkLevel level = " + level);
		level = level + 1;
		if (level < 0) {
			return 0;
		}
		// ���µ�īˮ��д����block
		setInkLevel(level, false);
		// ���µ�īˮ��д�ر���block
		setInkLevel(level, true);
		if (level == 0) {
			return 0;
		}
		//level = level/Configs.INK_LEVEL_MAX > 1 ? level/Configs.INK_LEVEL_MAX : 1;
		return level;
	}
	/**
	 * �������ȡ
	 */
	public byte[] getFeatureCode() {
		int errno = 0;
		
		if ( !keyVerfication(SECTOR_FEATURE, BLOCK_FEATURE, RFID_DEFAULT_KEY_A))
		{
			return null;
		}
		byte[] feature = readBlock(SECTOR_FEATURE, BLOCK_FEATURE);
		System.out.println( "************feature code***********");
		System.out.println(StringUtil.byteArrayToString(feature, feature.length));
		
		System.out.println( "************feature code***********");
		return feature;
	}
	
	public void setKeyA(byte[] key) {
		if (key == null || key.length != 6) {
			return ;
		}
		mRFIDKeyA = key;
		return;
	}
	
	public void makeCard() {
		//�޸���Կ
		/*
		if (!keyVerfication(SECTOR_FEATURE, BLOCK_KEY, RFID_DEFAULT_KEY_A)) {
			System.out.println( "===>makeCard key verify fail");
			return;
		}
		byte[] key = readBlock(SECTOR_FEATURE, BLOCK_KEY);
		for (int i = 0; i < 6; i++) {
			key[i] = (byte) (mRFIDKeyA[i] & 0x0ff);
			key[10+i] = (byte) (mRFIDKeyA[i] & 0x0ff);
		}
		//��д��Կ,��Կ�����ÿ��sector��block3
		writeBlock(SECTOR_FEATURE, BLOCK_KEY, key);
		*/
		//������
//		if (!keyVerfication(SECTOR_FEATURE, BLOCK_FEATURE, mRFIDKeyA)) {
//			System.out.println( "===>makeCard key verify fail");
//		}
//		byte[] block = readBlock(SECTOR_FEATURE, BLOCK_FEATURE);
//		Debug.print(block);
		System.out.println( "=============================");
		//byte[] keya = {0x11, 0x11, 0x11, 0x11, 0x11, 0x11};
		if (!keyVerfication((byte)6, (byte)0, RFID_DEFAULT_KEY_A)) {
			System.out.println( "===>makeCard key verify fail");
		}
		
		byte[] block = readBlock((byte)6, (byte)0);
		//Debug.print(block);
		System.out.println(StringUtil.byteArrayToString(block, block.length));
		for(int i = 0; i < 6; i++) {
			block[i] = 0x11;
		}
		writeBlock((byte)6, (byte)0, block);
		
		block = readBlock((byte)6, (byte)0);
		System.out.println(StringUtil.byteArrayToString(block, block.length));
		
		//byte[] feature = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,0x0A, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
		//writeBlock(SECTOR_FEATURE, BLOCK_FEATURE, feature);
	}
	
	private boolean isLevelValid(int value) {
		if (value < INK_LEVEL_MIN || value > INK_LEVEL_MAX) {
			return false;
		}
		return true;
	}
}
