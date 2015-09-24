package comm.rtx;
import java.util.*;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.util.Enumeration;  
import java.util.TooManyListenersException; 
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import com.sun.xml.internal.ws.util.StringUtils;
//import org.eclipse.swt.*;
import gnu.io.*;
//import gnu.io.SerialPortEventListener;


public class Form implements SerialPortEventListener, ActionListener{
	
	private static final int MAX_INK_LEVEL = 10000000;
	private static final int DEFAULT_INK_LEVEL = 100000;
	public  JFrame f= new JFrame("�ƿ�����");	
	public Container con = f.getContentPane();
    public   Toolkit toolkit= Toolkit.getDefaultToolkit();
    public  Dimension sc= toolkit.getScreenSize();
    public JLabel label1= new JLabel("COM��:");
    public JLabel label2= new JLabel("��ʼīˮ��:");
    public JLabel label3= new JLabel("����λ��");
    public JLabel label4= new JLabel("У��λ��");
    public JLabel label5= new JLabel("ֹͣλ��");
    public JLabel label6= new JLabel("��������");
    public JLabel label7= new JLabel("");
    public JTextField mInklevel = new JTextField();
    
    public byte[] SN;
    public byte[] mContent;
    public JLabel status= new JLabel("");
    public JTextArea textArea1= new JTextArea();
    
    
    // ���������ʾ
    public JLabel mResultJLabel = new JLabel("�������:");
    
    public JLabel mInkLabel = new JLabel("��ǰīˮ��:");
    public JLabel mInkState = new JLabel("");
    public JLabel mInkLabelMax = new JLabel("��īˮ��:");
    public JLabel mInkMaxState = new JLabel("");
    public JLabel mFeatureLabel = new JLabel("����ֵ:");
    public JLabel mFeature = new JLabel("");
    //public JTextArea textArea2= new JTextArea(); //fasongshuju  
   // String[] str = new String[] { "COM1", "COM2","COM3","COM4","COM5"};
    String[] parIty = new String[] { "NONE", "OLD", "EVEN", "MARK", "SPACE" };
    Integer[] baudrateCode = new Integer[] { 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200 };
    Integer[] DATA = new Integer[] { 6,7,8 };
    Integer[] STOP = new Integer[] { 1,2 };
    public JComboBox<String> jComboBox1= new JComboBox<String>();//���ں�
    public JComboBox<Integer> jComboBox2= new JComboBox<Integer>(baudrateCode);//������
    public JComboBox<Integer> jComboBox3= new JComboBox<Integer>(DATA);//����λ
    public JComboBox<String>  jComboBox4= new JComboBox<String>(parIty);//У��λ
    public JComboBox<Integer> jComboBox5= new JComboBox<Integer>(STOP);//ֹͣλ
 
    /**
     * ��īˮ��
     */
    public JLabel	mMaxInkLabel = new JLabel("��īˮ��:");
    public JTextField mMaxInk = new JTextField();
    
    /**
     * ����ֵ
     */
    public JLabel	mFeatureTitle = new JLabel("����ֵ:");
    public JButton   mOpenFile = new JButton("����ļ�");
    public JTextField Feature0	= new JTextField();
    public JTextField Feature1	= new JTextField();
    public JTextField Feature2	= new JTextField();
    public JTextField Feature3	= new JTextField();
    public JTextField Feature4	= new JTextField();
    public JTextField Feature5	= new JTextField();
    public JTextField Feature6	= new JTextField();
    public JTextField Feature7	= new JTextField();
    public JTextField Feature8	= new JTextField();
    public JTextField Feature9	= new JTextField();
    public JTextField Feature10	= new JTextField();
    public JTextField Feature11	= new JTextField();
    public JTextField Feature12	= new JTextField();
    public JTextField Feature13	= new JTextField();
    public JTextField Feature14	= new JTextField();
    public JTextField Feature15	= new JTextField();
    JButton button1= new JButton("��");//�򿪴��ڰ�ť
    JButton button2= new JButton("�ر�");//�رմ��ڰ�ť
    JButton button3= new JButton("Ѱ��");
    JButton button4= new JButton("�˳�");
    JButton button5= new JButton("����ͻ");
    JButton button6= new JButton("����");
    JButton button7= new JButton("��Կ��֤");
    JButton button8= new JButton("�ƿ�");
   
    JPanel p1 = new JPanel();
    

    private Enumeration portList;

    private CommPortIdentifier portId;

    private OutputStream outputStream;

    private InputStream inputStream;

    private SerialPort serialPort;

    private Timer TimerDisplay;
    
    private FeaturesFile mFeaturesFile;

  //  private byte[] readRxBuffer = new byte[2048];

  //  private int readRxCount = 0;

	public Form()
	{
		  Initialize();
		  LoadFeatures();
		  Load();
		  Run();
		 // CommPortInit();
		  Event();
		 
		 
		  
	}
	
	public void LoadFeatures() {
		mFeaturesFile = new FeaturesFile();
		Feature0.setText(Integer.toString(mFeaturesFile.getFeatures()[0]));
		Feature1.setText(Integer.toString(mFeaturesFile.getFeatures()[1]));
		Feature2.setText(Integer.toString(mFeaturesFile.getFeatures()[2]));
		Feature3.setText(Integer.toString(mFeaturesFile.getFeatures()[3]));
	    Feature4.setText(Integer.toString(mFeaturesFile.getFeatures()[4]));
	    Feature5.setText(Integer.toString(mFeaturesFile.getFeatures()[5]));
	    Feature6.setText(Integer.toString(mFeaturesFile.getFeatures()[6]));
	    Feature7.setText(Integer.toString(mFeaturesFile.getFeatures()[7]));
	    Feature8.setText(Integer.toString(mFeaturesFile.getFeatures()[8]));
	    Feature9.setText(Integer.toString(mFeaturesFile.getFeatures()[9]));
	    Feature10.setText(Integer.toString(mFeaturesFile.getFeatures()[10]));
	    Feature11.setText(Integer.toString(mFeaturesFile.getFeatures()[11]));
	    Feature12.setText(Integer.toString(mFeaturesFile.getFeatures()[12]));
	    Feature13.setText(Integer.toString(mFeaturesFile.getFeatures()[13]));
	    Feature14.setText(Integer.toString(mFeaturesFile.getFeatures()[14]));
	    Feature15.setText(Integer.toString(mFeaturesFile.getFeatures()[15]));
	}
	public void Initialize()
	{
		//f.setVisible(true);
//		 con.setLayout(null);
        f.setSize(550,550);
 	    //f.setSize(sc.width/4,sc.height*10/25);
 	    f.setLocation(sc.width/3,sc.height/4);
// 	   con.setLayout(null);
//       
// 	  
//	    f.setSize(sc.width/4,sc.height*10/25);
//	    f.setLocation(sc.width/3,sc.height/4);
//	   
//	    Border border= BorderFactory.createEtchedBorder(Color.gray, Color.black);
//		Border borde= BorderFactory.createTitledBorder(border, "��������", TitledBorder.LEFT, TitledBorder.TOP, 
//				new Font("����", Font.BOLD, 14));
//		p1.setBorder(borde);
//		p1.setLayout (new GridLayout (0, 2, 5, 10));
//		//p1.setForeground(Color.red);
//		p1.setBounds(5, 5, 150, 200);
//	    f.setResizable(false);
//		///label1.setBounds(5, 5, 50, 50);
//	    label1.setForeground(Color.LIGHT_GRAY);
//	    label2.setForeground(Color.blue);
//	    label3.setForeground(Color.blue);
//	   button1.setForeground(Color.green);
//	   button1.setForeground(Color.red);
//	  
//	    con.add(p1);
//	    p1.add(label1);
//	    
//	    p1.add(jComboBox1);
//	    p1.add(label2);
//	    p1.add(jComboBox2);
//	    jComboBox2.setSelectedItem(baudrateCode[4]);
//	    p1.add(label3);
//	    p1.add(jComboBox3);
//	    jComboBox3.setSelectedItem(DATA[2]);
//	    p1.add(label4);	
//	    p1.add(jComboBox4);
//	    p1.add(label5);
//	    p1.add(jComboBox5);
//	    p1.add(button1);
//	    p1.add(button2);
 	    // ��һ��
		label1.setBounds(5, 10, 50, 30);
		jComboBox1.setBounds(70, 10, 100, 30);
		con.add(label1);
	    con.add(jComboBox1);
	    button1.setBounds(180, 10, 70, 30);
	    con.add(button1);
	    button2.setBounds(260, 10, 70, 30);
	    con.add(button2);
	    // �ڶ���
	    label2.setBounds(5, 60, 80, 30);
		mInklevel.setBounds(80, 60, 80, 30);
		mInklevel.setText(Integer.toString(DEFAULT_INK_LEVEL));
		con.add(label2);
	    con.add(mInklevel);
		
		// max level
	    mMaxInkLabel.setBounds(200, 60, 80, 30);
	    con.add(mMaxInkLabel);
	    mMaxInk.setBounds(280, 60, 80, 30);
	    mMaxInk.setText(Integer.toString(DEFAULT_INK_LEVEL));
	    con.add(mMaxInk);
	    
	    // Features
	    mFeatureTitle.setBounds(5, 110, 100, 30);
	    con.add(mFeatureTitle);
	    
	    mOpenFile.setBounds(60, 110, 100, 30);
	    con.add(mOpenFile);
	    
	    Feature0.setBounds(10, 150, 50, 30);
	    
	    con.add(Feature0);
	    Feature1.setBounds(70, 150, 50, 30);
	    con.add(Feature1);
	    Feature2.setBounds(130, 150, 50, 30);
	    con.add(Feature2);
	    Feature3.setBounds(190, 150, 50, 30);
	    con.add(Feature3);
	    Feature4.setBounds(250, 150, 50, 30);
	    con.add(Feature4);
	    Feature5.setBounds(310, 150, 50, 30);
	    con.add(Feature5);
	    Feature6.setBounds(370, 150, 50, 30);
	    con.add(Feature6);
	    Feature7.setBounds(430, 150, 50, 30);
	    con.add(Feature7);	    
	    
	    Feature8.setBounds(10, 190, 50, 30);
	    con.add(Feature8); 
	    Feature9.setBounds(70, 190, 50, 30);
	    con.add(Feature9);
	    Feature10.setBounds(130, 190, 50, 30);
	    con.add(Feature10);
	    Feature11.setBounds(190, 190, 50, 30);
	    con.add(Feature11);
	    Feature12.setBounds(250, 190, 50, 30);
	    con.add(Feature12);	    
	    Feature13.setBounds(310, 190, 50, 30);
	    con.add(Feature13);
	    Feature14.setBounds(370, 190, 50, 30);
	    con.add(Feature14);
	    Feature15.setBounds(430, 190, 50, 30);
	    con.add(Feature15);
	    // ������
	    //button3.setBounds(20,  90, 70, 50);
	    button4.setBounds(270,  260, 70, 50);
	    //button5.setBounds(160, 90, 70, 50);
	   button6.setBounds(150, 260, 70, 50);
	    //button7.setBounds(300, 90, 70, 50);
	    button8.setBounds(30, 260, 70, 50);
	    button6.setEnabled(false);
	    button8.setEnabled(false);
	    // ������
	    mResultJLabel.setBounds(5, 330, 130, 30);
	    mResultJLabel.setFont(new Font("label", 0, 20));
	    
	    mInkLabel.setBounds(5, 360, 80, 30);
	    mInkState.setBounds(90, 360, 200, 30); 
	    
	    mInkLabelMax.setBounds(5, 390, 80, 30);
	    mInkMaxState.setBounds(90, 390, 100, 30);
	    
	    mFeatureLabel.setBounds(5, 420, 50, 30);
	    mFeature.setBounds(70, 420, 400, 30);
	    
	    label7.setBounds(5, 460, 300, 30);
	    //textArea2.setBounds(60, 250, 400, 100);
	    
	    
	    con.add(label6);
	     con.add(label7);
	     con.add(textArea1);
	     //con.add(textArea2);
	     //con.add(button3);
	     con.add(button4);
	     //con.add(button5);
	     con.add(button6);
	    // con.add(button7);
	     con.add(button8);
	    // textArea1.add(scroll);
	   con.add(mResultJLabel);
	   con.add(mInkLabel);
	   con.add(mInkState);
	   con.add(mInkLabelMax);
	   con.add(mInkMaxState);
	   con.add(mFeatureLabel);
	   con.add(mFeature);
	   // con.add(scroll);
	    TimerDisplay = new Timer();
        con.add(status);
        jComboBox1.setEnabled(true);
        jComboBox2.setEnabled(true);
        jComboBox3.setEnabled(true);
        jComboBox4.setEnabled(true);
        jComboBox5.setEnabled(true);
        button1.setEnabled(true);
        button2.setEnabled(false);
       // buttonSend.setEnabled(false);
 	 
		
	}
	public void Load()
	{
		CommPortInit();
   //  jComboBox1.setSelectedIndex(1);
		
		
	}
	public void Run()
	{
		 f.setVisible(true);
		 while (!f.isDisplayable())
		 {
		  con.addNotify();
			 
			 
		 }
		 this.Close();
		
	}
	 public void Close() {//�رռ�ж���¼��������
		   if (serialPort != null)
		    serialPort.close();// �رմ���
		   TimerDisplay.cancel();
		   // ��ʽ�ص���dispose() �������ͷų�������������õ���Դ
		  }
	 public void Event() 
	 {
		
		 button1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				portList=CommPortIdentifier.getPortIdentifiers();
				while(portList.hasMoreElements())
				{
					portId=(CommPortIdentifier)portList.nextElement();
					if(portId.getPortType()==CommPortIdentifier.PORT_SERIAL)
					{
						if(portId.getName().equals(jComboBox1.getSelectedItem().toString()))
						{
						  CommportOpen();
						  button6.setEnabled(true);
						  button8.setEnabled(true);
						}
					}
				}
			}
		});
		 button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				 if (serialPort != null) {

				        serialPort.close();// �رմ���
				        jComboBox1.setEnabled(true);
				        jComboBox2.setEnabled(true);
				        jComboBox3.setEnabled(true);
				        jComboBox4.setEnabled(true);
				        jComboBox5.setEnabled(true);
				        button1.setEnabled(true);
				        button2.setEnabled(false);
				        button6.setEnabled(false);
				        button8.setEnabled(false);
				       }
			}
		});
		 
		button4.addActionListener(this);
		f.addWindowListener(new  WindowAdapter() {
		
		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			 if(JOptionPane.showConfirmDialog(null, "�Ƿ��˳�", "��ʾ", JOptionPane.YES_NO_OPTION)==JOptionPane.OK_OPTION)
			 {
				 //serialPort.close();
				 System.exit(1);
			 }
			 else
			 {
				 return;
			 }
		}
	});
		 
	button3.addActionListener(this);
	
	button5.addActionListener(this);

	button6.addActionListener(this);
	
	button7.addActionListener(this);
	button8.addActionListener(this);
	
	mOpenFile.addActionListener(this);
	} 
	
	 
	 public void CommPortInit() {
		 portList = CommPortIdentifier.getPortIdentifiers();// ö�ٶ˿�
		 while (portList.hasMoreElements()) {// ö�ٶ˿�(����)
			 portId = (CommPortIdentifier) portList.nextElement();
			 if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {// ֻȡ�����豸
				 jComboBox1.addItem(portId.getName());
			 }
		 }
	 }
	 
	 public void CommportOpen()
	 {
		 try {
			 if(serialPort!=null)
				 serialPort.close();
			 serialPort=(SerialPort)portId.open("commreadapp", 2000);
			 try {
				 outputStream=serialPort.getOutputStream();
				 try {
					 inputStream=serialPort.getInputStream();
					 try {
						 serialPort.addEventListener(this);
				
						 try {
							 serialPort.setSerialPortParams(19200, 8, 1,0);
					
							//status.setText("ϵͳ��ʾ��"+"����"+serialPort.getName()+"�����򿪣�������ʼ������");
							jComboBox1.setEnabled(false);
							jComboBox2.setEnabled(false);
							jComboBox3.setEnabled(false);
							jComboBox4.setEnabled(false);
							jComboBox5.setEnabled(false);
							button1.setEnabled(false);
							button2.setEnabled(true);
						 }
			
						 catch (UnsupportedCommOperationException e) {
							 
						 }
					// TODO: handle exception
					 } catch (TooManyListenersException e) {
				      //        + serialPort.getName() + "�����¼��쳣!!!");				    	      }
					 }
				 }
				 catch (IOException e) {
				 }
			
			 } 
			 catch (IOException e) {
			 }	
		 }
		 catch (PortInUseException ex) {
			 status.setText("ϵͳ��ʾ: " + "����" + serialPort.getName()
			      + "���쳣!!!");
		 }
		 serialPort.notifyOnDataAvailable(true);//�򿪼���
	}
	
	public ByteArrayBuffer mRxBuffer = new ByteArrayBuffer();;
	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("====" + arg0.getEventType());
		switch(arg0.getEventType())
		{
		case SerialPortEvent.BI://�ж�
		case SerialPortEvent.OE://����
		case SerialPortEvent.FE://����
		case SerialPortEvent.PE://��żУ�����
		case SerialPortEvent.CD://�ز�����
		case SerialPortEvent.CTS://����Դ���
		case SerialPortEvent.DSR://���ݱ���
		case SerialPortEvent.RI://�������
		case  SerialPortEvent.OUTPUT_BUFFER_EMPTY://�����������
			break;
		case SerialPortEvent.DATA_AVAILABLE://�����п��õ�����
			
			byte[] readbuffer= new byte[256];
			try {
				int numBytes=0;
				while(inputStream.available()>0)
				{
					numBytes = inputStream.read(readbuffer);
					mRxBuffer.write(readbuffer, 0, numBytes);
				}
				
				//readRxCount+=numBytes;
//				byte[] real = new byte[numBytes];
//				for (int i = 0; i < real.length; i++) {
//					real[i] = readbuffer[i];
//				}
				byte[] real = mRxBuffer.toByteArray();
				if (real == null || real.length < 2 || real[real.length-1] != 0x03 
						||(real[real.length-1] == 0x03 && real[real.length-2] == 0x10) ) {
					break;
				}
				mRxBuffer.reset();
				textArea1.append(StringUtil.byteArrayToString(real, real.length)+'\n');
				//  readRxCount=0;
				RFIDData data = new RFIDData(real, true);
				byte cmd = data.getCommand();
				System.out.println("response:[ " + StringUtil.byteArrayToString(real, real.length) +" ]");
				System.out.println("cmd = "+ Integer.toHexString(cmd));
				if (cmd == 0x47) { //����ͻ
					textArea1.append("UID�� " + StringUtil.byteArrayToString(data.getData(), data.getData().length));
					System.out.println("UID: " + StringUtil.byteArrayToString(data.getData(), data.getData().length));
					SN = new byte[4];
					byte[] ret = data.getData();
					for (int i = 0; i < SN.length; i++) {
						SN[i] = ret[i+1];
					}
				} else if (cmd == 0x4a) { //��Կ��֤���
					byte[] cert = data.getData();
					// System.out.println("��Կ��֤��" + StringUtil.byteArrayToString(cert, cert.length));
					if (cert[0] == 0x00) {
						mCertifyOk = true;
					} else {
						mCertifyOk = false;
					}
					System.out.println("��Կ��֤��" + mCertifyOk);
				}else if (cmd == 0x4B) {  //������
					mContent = new byte[16];
					byte[] ret = data.getData();
					for (int i = 0; i < mContent.length; i++) {
						mContent[i] = ret[i+1];
					}
				}
				if(textArea1.getLineCount()>10)
				{
					textArea1.setText("");
				}
				// ActionEvent event = new ActionEvent(button5, 0, button5.getActionCommand());
				//actionPerformed(event);
				mDataAvilable = true;
			}
				
			 catch (Exception e) {
				// TODO: handle exception
				 status.setText("ϵͳ��ʾ: " + "����"
				         + serialPort.getName() + "����ʧ��!!!");
				      };
			break;
			}
		
		}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("�˳�")) {
			if(JOptionPane.showConfirmDialog(null, "�Ƿ��˳�", "��ʾ", JOptionPane.YES_NO_OPTION)==JOptionPane.OK_OPTION)
			 {
				//serialPort.close();
				 System.exit(1);
			 }
		} else if (e.getActionCommand().equals("Ѱ��")) {
			if(serialPort!=null)
			{
				try {
				// Ѱ��
					// byte[] data = {0x02, 0x00, 0x00, 0x04, 0x46, 0x52, (byte) 0x9C, 0x03 };
					byte data[] = {0x52};
					RFIDData cmd = new RFIDData((byte)0x46, data);
					System.out.println(StringUtil.byteArrayToString(cmd.transferData(), cmd.transferData().length));
					outputStream.write(cmd.transferData());
					
				}
				catch (IOException e1) {
					status.setText("ϵͳ��ʾ: " + "����" + serialPort.getName() + "����ʧ��!!!");
				}
			}
		} else if (e.getActionCommand().equals("����ͻ")) {
			if(serialPort!=null)
			{
				// ����ͻ
				try {
					// byte[] data = {0x02, 0x00, 0x00, 0x04, 0x47, 0x04, 0x4F, 0x03 };
					byte[] data = {0x04};
					RFIDData cmd = new RFIDData((byte)0x47, data);
					outputStream.write(cmd.transferData());
				
				}
				catch (IOException e1) {
					status.setText("ϵͳ��ʾ: " + "����"
					          + serialPort.getName() + "����ʧ��!!!");
				}
			}
		} else if (e.getActionCommand().equals("ѡ��")) {
			if(serialPort!=null)
			{
				// ѡ��
				try {
					// byte[] data = {0x02 , 0x00 , 0x00 , 0x07 , 0x48 , (byte)0xC1, 0x2B, 0x7D, 0x25, (byte) 0xdd, 0x03};
					byte[] data = {(byte)0xC1, 0x2B, 0x7D, 0x25};
					RFIDData cmd = new RFIDData((byte)0x48, data);
					outputStream.write(cmd.transferData());
				}
				catch (IOException e1) {
					status.setText("ϵͳ��ʾ: " + "����"
					          + serialPort.getName() + "����ʧ��!!!");
				}
			}
		} else if (e.getActionCommand().equals("��Կ��֤")) {
			if(serialPort!=null)
			{
				keyCertify((byte)0x1c, mKeyA);
			}
		} else if (e.getActionCommand().equals("����")) {
			if(serialPort!=null)
			{
				// ����
				
					// byte[] data = {0x02, 0x00, 0x00, 0x04, 0x4B, 0x00, 0x4F, 0x03};
//					byte[] data = {0x13};
//					RFIDData cmd = new RFIDData((byte)0x4B, data);
//					outputStream.write(cmd.transferData());
				//����īˮ��
				//initCard();
				label7.setText("���������Ժ�...");
				System.out.println("��ʼ����....");
				byte block = (byte) (RFIDDevice.SECTOR_INKLEVEL * 4 + RFIDDevice.BLOCK_INKLEVEL);
				keyCertify(block, null);
				if (!(mDefaultCertifyOk = mCertifyOk)) {
					System.out.println("����---Ĭ����Կ��֤ʧ�ܣ�ʹ��Ψһ��Կ��֤");
					keyCertify(block, mKeyA);
					if (!(mUIDCertifyOk = mCertifyOk)) {
						System.out.println("����---Ψһ��Կ��֤ʧ��");
						return;
					}
				}
				System.out.println("����---��Կ��֤�ɹ�");
				System.out.println("��ʼ��ȡīˮ��...");
				byte[] ink = readBlock(block);
				EncryptionMethod method = EncryptionMethod.getInstance();
				int state = method.decryptInkLevel(ink);
				mInkState.setText(Integer.toString(state));
				
				System.out.println("��ʼ��ȡ��īˮ��...");
				block = (byte) (RFIDDevice.SECTOR_INK_MAX * 4 + RFIDDevice.BLOCK_INK_MAX);
				byte[] inkMax = readBlock(block);
				int max = method.decryptInkLevel(ink);
				mInkMaxState.setText(Integer.toString(max));
				System.out.println("����īˮ������" + max);
				
				System.out.println("��ʼ��ȡ����ֵ...");
				block = (byte) (RFIDDevice.SECTOR_FEATURE * 4 + RFIDDevice.BLOCK_FEATURE);
				byte[] features = readBlock(block);
				System.out.println("======����ֵ======");
				String feature="";
				for (int i = 0; i < features.length; i++) {
					feature += Integer.toString(features[i]) + "  ";
				}
				System.out.println(feature);
				System.out.println("======����ֵ======");
				mFeature.setText(feature);
				label7.setText("�������");
			}
		} else if (e.getActionCommand().equals("�ƿ�")) {
			new Thread(){
				 @Override
			     public void run() {
					 makeCard();
				 }
			}.start();
		} else if (e.getActionCommand().equals("����ļ�")) {
			FileDialog dialog = new FileDialog(new Frame(), "��", FileDialog.LOAD);
			FilenameFilter filter = new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					if (name.endsWith("txt")) {
						return true;
					}
					return false;
				}
			};
			dialog.setFilenameFilter(filter);
			dialog.addWindowListener(new WindowListener() {
				
				@Override
				public void windowOpened(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowIconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowDeiconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowDeactivated(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowClosing(WindowEvent arg0) {
					System.out.println("�ļ��Ի���ر���...");
				}
				
				@Override
				public void windowClosed(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowActivated(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			dialog.show();
			System.out.println(dialog.getFile());
			mFeaturesFile.setFile(dialog.getFile());
			LoadFeatures();
		}
		
	}
	
	public byte[] mUID;
	public byte[] mKeyA;
	public boolean mCertifyOk;
	public boolean mDefaultCertifyOk;
	public boolean mUIDCertifyOk;
	public boolean mDataAvilable=false;
	public static byte[] RFID_DATA_SEARCHCARD_WAKE = {0x26};
	public static byte[] RFID_DATA_SEARCHCARD_ALL = {0x52};
	public static byte[] RFID_DATA_AVOIDCONFLICT= {0x04};
	public static byte[] RFID_DATA_KEYCERT = {0x60, 0x13, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
	public void makeCard() {
		label7.setText("�ƿ���...");
		label7.setForeground(Color.BLACK);
		mCertifyOk = false;
		mDefaultCertifyOk = false;
		mUIDCertifyOk = false;
		
				initCard();
				
				// �޸���Կ
				modifyKey(RFIDDevice.SECTOR_INKLEVEL);
				// �޸ı���Sector��Կ
				modifyKey(RFIDDevice.SECTOR_COPY_INKLEVEL);
				
				// �޸�����ֵSector��Կ
				// modifyKey(RFIDDevice.SECTOR_COPY_INKLEVEL);
				
				System.out.println("=====�޸���Կok====");
				
				System.out.println("=====��ʼдINK LEVEL��ʼֵ===");
				// д���ʼֵ
				byte block = (byte)(RFIDDevice.SECTOR_INKLEVEL * 4 + RFIDDevice.BLOCK_INKLEVEL);
				keyCertify(block, mKeyA);
				System.out.println("ʹ��Ψһ��Կ��֤�����" + mCertifyOk);
				
				if (!(mUIDCertifyOk = mCertifyOk)) {
					label7.setText("ʧ�ܣ���Կ��֤��Ч������");
					label7.setForeground(Color.RED);
					return;
				}
				System.out.println("=====д��ʼֵ��INK LEVEL===");
				// ��������ȡ��ʼīˮ��
				String text = mInklevel.getText();
				int inkLevel = 0;
				try {
					inkLevel = Integer.parseInt(text);
				} catch (NumberFormatException e) {
					inkLevel = DEFAULT_INK_LEVEL;
				}
				
				if (inkLevel <= 0 || inkLevel > MAX_INK_LEVEL) {
					inkLevel = DEFAULT_INK_LEVEL;
				}
				byte[] l = EncryptionMethod.getInstance().encryptInkLevel(inkLevel);
				System.out.println("��ʼֵ:" + StringUtil.byteArrayToString(l, l.length));
				writeBlock(block, l);
				
				System.out.println("=====��ʼ�ָ����ݿ�===");
				// ������д���ʼֵ
				
				block = (byte)(RFIDDevice.SECTOR_COPY_INKLEVEL * 4 + RFIDDevice.BLOCK_COPY_INKLEVEL);
				keyCertify(block, mKeyA);
				System.out.println("ʹ��Ψһ��Կ��֤�����" + mCertifyOk);
				if (!(mUIDCertifyOk = mCertifyOk)) {
					label7.setText("ʧ�ܣ���Կ��֤��Ч������");
					label7.setForeground(Color.RED);
					return;
				}
				System.out.println("=====д��ʼֵ��INK LEVEL���ݿ�===");
				writeBlock(block, l);
				
				// д����ֵ
				writeFeatures();
				
				// д��īˮ��
				writeTotalInk();
				
				label7.setText("�ƿ��ɹ���");
				label7.setForeground(Color.BLUE);
			
	}
	
	public void modifyKey(byte sector) {
		// Ĭ����ԿA��֤��ȷ���Ƿ�Ϊԭʼ��
		mCertifyOk = false;
		byte block = (byte)(sector * 4 + RFIDDevice.BLOCK_KEY);
		System.out.println("ʹ��Ĭ����Կ��֤...");
		keyCertify(block, null);
		// ���Ĭ����Կ��֤ʧ�ܣ���ʹ��Ψһ��Կ��֤
		if (!(mDefaultCertifyOk = mCertifyOk)) {
			System.out.println("ʹ��Ψһ��Կ��֤...");
			mCertifyOk = false;
			keyCertify(block, mKeyA);
			if (!(mUIDCertifyOk = mCertifyOk)) {
				label7.setText("ʧ�ܣ���Կ��֤��Ч������");
				label7.setForeground(Color.RED);
				return;
			}
		}
		if (mDefaultCertifyOk) {
			System.out.println("�޸���ԿΪΨһ��Կ");
			readBlock((byte)(sector * 4 + RFIDDevice.BLOCK_KEY));
			for (int i = 0; i < 6; i++) {
				mContent[i] = mKeyA[i];
			}
			
			writeBlock((byte)(sector * 4 + RFIDDevice.BLOCK_KEY), mContent);
		}
		
		
		
	}
	
	private void keyCertify(byte block, byte[] key) {
		initCard();
		try{
			byte[] data = getKeyBytes();
			data[1] = block;
			if (key != null) {
				for (int i = 0; i < 6; i++) {
					data[2+i] = key[i];
				}
			}
			RFIDData cmd = new RFIDData((byte)0x4A, data);
			mDataAvilable = false;
			mCertifyOk = false;
			mDefaultCertifyOk = false;
			outputStream.write(cmd.transferData());
			System.out.println("==>keyCertify: " + StringUtil.byteArrayToString(cmd.transferData(), cmd.transferData().length));
			while (!mDataAvilable) {
				Thread.sleep(100);
			}
		} catch(IOException e) {
			
		} catch (InterruptedException e) {
			
		}
	}
	
	private void keyCertifyWithoutInit(byte block, byte[] key) {
		try{
			byte[] data = getKeyBytes();
			data[1] = block;
			if (key != null) {
				for (int i = 0; i < 6; i++) {
					data[2+i] = key[i];
				}
			}
			RFIDData cmd = new RFIDData((byte)0x4A, data);
			mDataAvilable = false;
			mCertifyOk = false;
			mDefaultCertifyOk = false;
			outputStream.write(cmd.transferData());
			System.out.println("==>keyCertify: " + StringUtil.byteArrayToString(cmd.transferData(), cmd.transferData().length));
			while (!mDataAvilable) {
				Thread.sleep(100);
			}
		} catch(IOException e) {
			
		} catch (InterruptedException e) {
			
		}
	}
	
	private void writeBlock(byte block, byte[] data) {
		
		try {
			ByteArrayBuffer buffer = new ByteArrayBuffer();
			buffer.write(block);
			buffer.write(data);
			RFIDData cmd = new RFIDData((byte)0x4C, buffer.toByteArray());
			mDataAvilable = false;
			outputStream.write(cmd.transferData());
			System.out.println("==>writeBlock: " + StringUtil.byteArrayToString(cmd.transferData(), cmd.transferData().length));
			while (!mDataAvilable) {
				Thread.sleep(100);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private byte[] readBlock(byte block) {
		byte[] data = new byte[1];
		data[0] = block;
		RFIDData cmd = new RFIDData((byte)0x4B, data);
		try {
			outputStream.write(cmd.transferData());
			System.out.println("==>readBlock: " + StringUtil.byteArrayToString(cmd.transferData(), cmd.transferData().length));
			mDataAvilable = false;
			while (!mDataAvilable) {
				Thread.sleep(100);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return mContent;
	}
	
	private byte[] getKeyBytes() {
		byte[] key = new byte[8];
		key[0] = 0x60;
		for (int i = 2; i < key.length; i++) {
			key[i] = (byte) 0x0FF;
		}
		return key;
	}
	
		public void writeFeatures() {
		byte[] features = new byte[16]; 
		features[0] = (byte) Integer.parseInt(Feature0.getText());
		features[1] = (byte) Integer.parseInt(Feature1.getText());
		features[2] = (byte) Integer.parseInt(Feature2.getText());
		features[3] = (byte) Integer.parseInt(Feature3.getText());
		features[4] = (byte) Integer.parseInt(Feature4.getText());
		features[5] = (byte) Integer.parseInt(Feature5.getText());
		features[6] = (byte) Integer.parseInt(Feature6.getText());
		features[7] = (byte) Integer.parseInt(Feature7.getText());
		features[8] = (byte) Integer.parseInt(Feature8.getText());
		features[9] = (byte) Integer.parseInt(Feature9.getText());
		features[10] = (byte) Integer.parseInt(Feature10.getText());
		features[11] = (byte) Integer.parseInt(Feature11.getText());
		features[12] = (byte) Integer.parseInt(Feature12.getText());
		features[13] = (byte) Integer.parseInt(Feature13.getText());
		features[14] = (byte) Integer.parseInt(Feature14.getText());
		features[15] = (byte) Integer.parseInt(Feature15.getText());
		byte block = (byte)(RFIDDevice.SECTOR_FEATURE*4 + RFIDDevice.BLOCK_FEATURE);
		keyCertify(block, mKeyA);
		if (mUIDCertifyOk) {
			writeBlock(block, features);
		}
		
	}
	
	public void writeTotalInk() {
		byte block = (byte) (RFIDDevice.SECTOR_INK_MAX * 4 + RFIDDevice.BLOCK_INK_MAX);
		keyCertify(block, mKeyA);
		int max = Integer.parseInt(mMaxInk.getText());
		EncryptionMethod encrypt = EncryptionMethod.getInstance();
		if (mUIDCertifyOk) {
			writeBlock(block, encrypt.encryptInkLevel(max));
		}
	}
	
	private void initCard() {
		try {
			// Ѱ��
			// byte[] data = {0x02, 0x00, 0x00, 0x04, 0x46, 0x52, (byte) 0x9C, 0x03 };
			byte data[] = RFID_DATA_SEARCHCARD_ALL;
			RFIDData cmd = new RFIDData((byte)0x46, data);
			System.out.println(StringUtil.byteArrayToString(cmd.transferData(), cmd.transferData().length));
			
			outputStream.write(cmd.transferData());
			
			mDataAvilable = false;
			while (!mDataAvilable) {
				Thread.sleep(100);
			}
			System.out.println("======Ѱ�� ok=====");
			// ����ͻ
			// byte[] data = {0x02, 0x00, 0x00, 0x04, 0x47, 0x04, 0x4F, 0x03 };
			data = RFID_DATA_AVOIDCONFLICT;
			cmd = new RFIDData((byte)0x47, data);
			outputStream.write(cmd.transferData());
			mDataAvilable = false;
			while (!mDataAvilable) {
				Thread.sleep(100);
			}
			System.out.println("=====����ͻ ok======");
			// ѡ��
			// byte[] data = {0x02 , 0x00 , 0x00 , 0x07 , 0x48 , (byte)0xC1, 0x2B, 0x7D, 0x25, (byte) 0xdd, 0x03};
			data = new byte[4];
			for (int i = 0; i < data.length; i++) {
				data[i] = SN[i];
			}
			cmd = new RFIDData((byte)0x48, data);
			outputStream.write(cmd.transferData());
			mDataAvilable = false;
			while (!mDataAvilable) {
				Thread.sleep(100);
			}
			System.out.println("=====ѡ��ok======");
			
			//���豸UID - ��Կ��֤
			mCertifyOk = false;
			keyCertifyWithoutInit((byte) 0, null);
			if (!mCertifyOk) {
				return;
			}
			System.out.println("=====��Կ��֤ok======");
			readBlock((byte)0);
			System.out.println("=====��UID ======");
			mUID = new byte[4];
			for (int i = 0; i < 4; i++) {
				mUID[i] = mContent[i];
			}
			System.out.println(StringUtil.byteArrayToString(mUID, mUID.length));
			// ������Կ
			mKeyA = EncryptionMethod.getInstance().getKeyA(mUID);
			System.out.println("��ԿA��" + StringUtil.byteArrayToString(mKeyA, mKeyA.length));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	}
	

	
	

