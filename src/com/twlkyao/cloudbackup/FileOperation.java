package com.twlkyao.cloudbackup;


import java.io.FileNotFoundException;
import java.io.IOException;
import net.lingala.zip4j.exception.ZipException;

import com.twlkyao.utils.BigFileDecrypt;
import com.twlkyao.utils.CharSet;
import com.twlkyao.utils.DigestMethod;
import com.twlkyao.utils.FileDEncryption;
import com.twlkyao.utils.FileInfo;
import com.twlkyao.utils.FileZ4J;
import com.twlkyao.utils.FileZip;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class FileOperation extends Activity {

	/**
	 * 
	 * appid: ����������ID
	 * sha : ���ϴ��ļ���Ӧ��SHA1��ֵ��ʹ��SHA1�㷨��
	 * md5 : ���ϴ��ļ���Ӧ��MD5ֵ
	 * size : �ļ���С����λΪ�ֽ�
	 * name : �ļ������ַ���������������UTF8��ʽ���룬�ļ��������windowsϵͳһ����
		�ļ����в��ܰ��������ַ� \ / : * ? �� < > | ��
	 * upload_type:�ϴ����ͣ���ѡ��control:�ؼ��ϴ���Ĭ�ϣ���normal:��ͨhttp�ϴ���
		���ݵ��ϴ���ʽһ��Ҫ��д��ȷ����Ȼ������ȷ�ϴ�����Ϊ��ͬ���ϴ���ʽ�����ص��ϴ��������ǲ�һ��
	 */
	private Button btn_upload;			//�����ϴ��ļ�
	private Button btn_download;		//���������ļ�
	private Button btn_make_dir;		//�������ƶ����Ŀ¼
	private Button btn_encryption;		//���ڼ��ܱ����ļ�
	private Button btn_decryption;		//���ڽ��ܼ��ܺ���ļ�
	private Button btn_zip;				//����ѹ���ļ�
	private Button btn_unzip;			//���ڽ�ѹ�ļ�
	public static int upload = 1;		//�ϴ��ļ�������
	public static int download = 2;		//�����ļ�������
	public static int make_dir = 3;		//�½�Ŀ¼������
	public static int encryption = 4;	//�ļ�����������
	public static int decryption = 5;	//�ļ�����������
	public static int zip = 6;			//�ļ�ѹ��������
	public static int unzip = 7;		//�ļ���ѹ��������
	String fileName;	//���ڼ�¼�ļ���
	String filePath;	//���ڼ�¼�ļ�·��
	String SHA1;		//���ڼ�¼�ļ���SHA1ֵ
	String MD5;			//���ڼ�¼�ļ���MD5ֵ
	String strKey = "1234567812345678123456781234567812345678123456781234567812345678";		//�ӽ�����Կ64λ
	String alg = "DES";
	long sizeB;			//���ڼ�¼�ļ��Ĵ�С����λΪ�ֽ�
	String upload_type;	//���ڼ�¼�ļ����ϴ�
	DigestMethod digestMethod = new DigestMethod();
	CharSet charSet = new CharSet();	//�����ַ�����
	private String urlStr;	//���ڹ���HTTP�����ַ
    private String urlBase = "https://graph.qq.com/weiyun/put?";	//����Ļ�����ַ
	FileDEncryption fileDEncryption = new FileDEncryption();	//ʵ����һ���ļ����ܶ���
	BigFileDecrypt bigFileDecrypt = new BigFileDecrypt();
	FileInfo fileInfo = new FileInfo();	//ʵ����һ���ļ���д����
	FileZip fileZip = new FileZip();	//ʵ����һ����ѹ������
    private BasicHttpClient basicHttpClient = new BasicHttpClient();
    private ApacheHttpClient apacheHttpClient = new ApacheHttpClient();	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	/**
         * 2.3��ʼ�ṩ��һ���µ��� StrictMode������������ڲ�׽������Ӧ�ó������߳��к�ʱ�Ĵ��̡�
         * ������ʻ������ã����԰��������߸Ľ�����ʹ���̴߳��� UI �Ͷ����ڴ��̶�д���������ʱ��ø�ƽ����
         * �������̱߳�������������Ϊ���������ӵ�Ҫ����ϸ��ˡ�
         */
        String strVer = android.os.Build.VERSION.RELEASE;	//��õ�ǰϵͳ�İ汾
        strVer = strVer.substring(0,3).trim();				//����汾��
        float floatversion = Float.valueOf(strVer);
        if(floatversion > 2.3) {
        	 /*��������������������������������������������������Android3.0��ʼ���粻�������߳��С�������������������������*/
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()       
            .detectDiskReads()       
            .detectDiskWrites()       
            .detectNetwork()   // or .detectAll() for all detectable problems       
            .penaltyLog()       
            .build());       
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()       
            .detectLeakedSqlLiteObjects()    
            .penaltyLog()       
            .penaltyDeath()       
            .build()); 
            /*��������������������������������������������������Android3.0��ʼ���粻�������߳��С�������������������������*/
        }
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_operation);
        
        findViews();			//������Ӧ�ؼ�
        setListeners();			//���ü�����
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_file_operation, menu);
        return true;
    }
    
  //������Ӧ�ؼ�
    public void findViews() {
    	btn_upload = (Button) this.findViewById(R.id.btn_upload);		//�ϴ���ť
    	btn_download = (Button) this.findViewById(R.id.btn_download);	//���ذ�ť
    	btn_make_dir = (Button) this.findViewById(R.id.btn_make_dir);	//�½�Ŀ¼
    	btn_encryption = (Button) this.findViewById(R.id.btn_encryption);	//�����ļ�
    	btn_decryption = (Button) this.findViewById(R.id.btn_decryption);	//�����ļ�
    	btn_zip = (Button) this.findViewById(R.id.btn_zip);
    	btn_unzip = (Button) this.findViewById(R.id.btn_unzip);
    }
    
    //ʵ�ּ�����
    public void setListeners() {
    	
    	//ʵ���ϴ���ť������
    	btn_upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//�½�һ��Intentʵ������������Activity��ת
				Bundle bundle = new Bundle();										//�½�һ��Bundle��������Intent������
				bundle.putInt("FileOperation", upload);					//����ֵ�Է���Bundle��
				intent.putExtras(bundle);					//��Bundle���ݷ���Intent��
				startActivityForResult(intent, upload);		//ѡ���ļ����ϴ�
			}
		});
    	
    	//ʵ�����ذ�ť������
    	btn_download.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//�½�һ��Intentʵ������������Activity��ת
				Bundle bundle = new Bundle();										//�½�һ��Bundle��������Intent������
				bundle.putInt("FileOperation", download);					//����ֵ�Է���Bundle��
				intent.putExtras(bundle);					//��Bundle���ݷ���Intent��
				startActivityForResult(intent, download); 	//ѡ���ļ�������	
			}
		});
    	
    	//ʵ���½�Ŀ¼������
    	btn_make_dir.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//�½�һ��Intentʵ������������Activity��ת
				Bundle bundle = new Bundle();										//�½�һ��Bundle��������Intent������
				bundle.putInt("FileOperation", make_dir);				//����ֵ�Է���Bundle��
				intent.putExtras(bundle);					//��Bundle���ݷ���Intent��
				startActivityForResult(intent, make_dir); 	//ѡ���ļ�������	
			}
		});
    	
    	//ʵ���ļ�����
    	btn_encryption.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//�½�һ��Intentʵ������������Activity��ת
				Bundle bundle = new Bundle();										//�½�һ��Bundle��������Intent������
				bundle.putInt("FileOperation", encryption);	//����ֵ�Է���Bundle��
				intent.putExtras(bundle);					//��Bundle���ݷ���Intent��
				startActivityForResult(intent, encryption); //ѡ���ļ�������
			}
		});
    	
    	//ʵ���ļ�����
    	btn_decryption.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//�½�һ��Intentʵ������������Activity��ת
				Bundle bundle = new Bundle();										//�½�һ��Bundle��������Intent������
				bundle.putInt("FileOperation", decryption);	//����ֵ�Է���Bundle��
				intent.putExtras(bundle);					//��Bundle���ݷ���Intent��
				startActivityForResult(intent, decryption); //ѡ���ļ�������
			}
		});
    	
    	//ʵ���ļ�ѹ��
    	btn_zip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//�½�һ��Intentʵ������������Activity��ת
				Bundle bundle = new Bundle();										//�½�һ��Bundle��������Intent������
				bundle.putInt("FileOperation", zip);	//����ֵ�Է���Bundle��
				intent.putExtras(bundle);				//��Bundle���ݷ���Intent��
				startActivityForResult(intent, zip); 	//ѡ���ļ���ѹ��
				
				
			}
		});
    	
    	//ʵ���ļ���ѹ��
    	btn_unzip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//�½�һ��Intentʵ������������Activity��ת
				Bundle bundle = new Bundle();										//�½�һ��Bundle��������Intent������
				bundle.putInt("FileOperation", unzip);	//����ֵ�Է���Bundle��
				intent.putExtras(bundle);				//��Bundle���ݷ���Intent��
				startActivityForResult(intent, unzip); 	//ѡ���ļ���ѹ��
			}
		});
    }

   
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if(upload == resultCode) {
			Log.i("result", "�ϴ�");
			fileName = data.getStringExtra("name");
			filePath = data.getStringExtra("file_path");
			Log.i("name", fileName);
			Log.i("file_path", filePath);
			try {
				SHA1 = digestMethod.byte2Str(digestMethod.SHA1(filePath));	//��ȡ�ļ���SHA1ֵ
				MD5 = digestMethod.byte2Str(digestMethod.MD5(filePath));	//��ȡ�ļ���MD5ֵ
				sizeB = digestMethod.fileSize(filePath);					//��ȡ�ļ��Ĵ�С
				
//				Log.i("FileSize", sizeB + "");								//����ļ��Ĵ�С����λΪ�ֽ�
				Log.i("fileSize", digestMethod.fileSize(filePath) + "");	//����ļ��Ĵ�С����λΪ�ֽ�
//				Log.i("getSize", digestMethod.getSize(filePath) + "");		//����ļ��Ĵ�С����λΪ�ֽ�
				Log.i("SHA1", SHA1);
				Log.i("MD5", MD5);
				
//				urlStr = urlBase + "access_token=" + CloudBackupActivity.access_token + "&oauth_consumer_key=" + CloudBackupActivity.appid 
//						+ "&openid=" + CloudBackupActivity.openid + "&sha=" + SHA1 + "&md5=" + MD5 + "&size=" + sizeB + "&name=" + charSet.toUTF_8(fileName);
				urlStr = urlBase + "access_token=" + CloudBackupActivity.access_token + "&oauth_consumer_key=" + CloudBackupActivity.appid + "&openid=" + CloudBackupActivity.openid;
				System.out.println("urlStr:" + urlStr);
				System.out.println("BasicHttpClient�����" + basicHttpClient.httpGet(urlStr));
				System.out.println("ApacheHttpClient�����" + apacheHttpClient.httpGet(urlStr));
				
				/**
				 * ʵ��HttpGet��ʽ
				 */
				/*HttpClient httpClient = new DefaultHttpClient();	//����һ��HttpClient����
				HttpGet httpGet = new HttpGet(urlStr);
				System.out.println("httpGetִ��");
				HttpResponse httpResponse = httpClient.execute(httpGet);	//����execute()�����������󲢷�����Ӧ
				HttpEntity entity = httpResponse.getEntity();	//HttpClient����Ӧ���ݷ�װΪHttpClient
				int statusCode = httpResponse.getStatusLine().getStatusCode();	//���״̬��
				System.out.println("��Ӧ�룺" + statusCode);*/
				/*if(entity != null) {
					InputStream inputStream = entity.getContent();
					int l;
					byte[] tmp = new byte[2048];
					while((l = inputStream.read(tmp)) != -1) {
						
					}
				}*/
				
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(download == resultCode) {
			Log.i("result", "����");
			fileName = data.getStringExtra("name");
			filePath = data.getStringExtra("file_path");
			Log.i("name", fileName);
			Log.i("file_path", filePath);
				
		} else if(make_dir == resultCode) {
			Log.i("result", "�½�Ŀ¼");
		} else if(encryption == resultCode) {
			Log.i("result", "�����ļ�");
			fileName = data.getStringExtra("name");
			filePath = data.getStringExtra("file_path");
//			bigFileDecrypt.enCrByteFile(filePath, filePath, strKey);
			fileDEncryption.encrypt(filePath, filePath, strKey, alg);
//			fileInfo.writeFile(filePath, fileDEncryption.encrypt(filePath, strKey, "DES"));
			Log.i("����Ŀ¼", filePath);
		} else if(decryption == requestCode) {
			Log.i("result", "�����ļ�");
			fileName = data.getStringExtra("name");
			filePath = data.getStringExtra("file_path");
//			bigFileDecrypt.deCrByteFile(filePath, filePath, strKey);
			fileDEncryption.decrypt(filePath, filePath, strKey, alg);
//			fileInfo.writeFile(filePath, fileDEncryption.decrypt(filePath, strKey, "DES"));
			Log.i("����Ŀ¼", filePath);
		} else if(zip == resultCode) {
			Log.i("result", "ѹ��");
			fileName = data.getStringExtra("name");
			filePath = data.getStringExtra("file_path");
			
			Thread thread = new Thread(new Runnable(){	//�����µ��߳�����ѹ���ļ�
				public void run() {
					FileZ4J filez4j = new FileZ4J();
					filez4j.zip(filePath, filePath + ".zip", false, "");
					}
				}); // �������߳�
			thread.start(); // �����߳�
			
			
			Log.i("name", fileName);
			Log.i("file_path", filePath);
		} else if(unzip == resultCode) {
			Log.i("result", "��ѹ��");
			fileName = data.getStringExtra("name");
			filePath = data.getStringExtra("file_path");
			
			Thread thread = new Thread(new Runnable(){
				public void run() {
					FileZ4J filez4j = new FileZ4J();
					try {
						filez4j.unzip(filePath, "");
					} catch (ZipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			thread.start();
			
			Log.i("name", fileName);
			Log.i("file_path", filePath);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

}
