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
	 * appid: 第三方接入ID
	 * sha : 待上传文件对应的SHA1的值（使用SHA1算法）
	 * md5 : 待上传文件对应的MD5值
	 * size : 文件大小，单位为字节
	 * name : 文件名，字符串（中文名称用UTF8格式编码，文件名规则跟windows系统一样，
		文件名中不能包括下列字符 \ / : * ? ” < > | ）
	 * upload_type:上传类型，可选。control:控件上传（默认），normal:普通http上传。
		数据的上传方式一定要填写正确，不然不能正确上传，因为不同的上传方式，返回的上传服务器是不一样
	 */
	private Button btn_upload;			//用于上传文件
	private Button btn_download;		//用于下载文件
	private Button btn_make_dir;		//用于在云端添加目录
	private Button btn_encryption;		//用于加密本地文件
	private Button btn_decryption;		//用于解密加密后的文件
	private Button btn_zip;				//用于压缩文件
	private Button btn_unzip;			//用于解压文件
	public static int upload = 1;		//上传文件请求码
	public static int download = 2;		//下载文件请求码
	public static int make_dir = 3;		//新建目录请求码
	public static int encryption = 4;	//文件加密请求码
	public static int decryption = 5;	//文件解密请求码
	public static int zip = 6;			//文件压缩请求码
	public static int unzip = 7;		//文件解压缩请求码
	String fileName;	//用于记录文件名
	String filePath;	//用于记录文件路径
	String SHA1;		//用于记录文件的SHA1值
	String MD5;			//用于记录文件的MD5值
	String strKey = "1234567812345678123456781234567812345678123456781234567812345678";		//加解密密钥64位
	String alg = "DES";
	long sizeB;			//用于记录文件的大小，单位为字节
	String upload_type;	//用于记录文件的上传
	DigestMethod digestMethod = new DigestMethod();
	CharSet charSet = new CharSet();	//用于字符编码
	private String urlStr;	//用于构造HTTP请求地址
    private String urlBase = "https://graph.qq.com/weiyun/put?";	//请求的基础地址
	FileDEncryption fileDEncryption = new FileDEncryption();	//实例化一个文件加密对象
	BigFileDecrypt bigFileDecrypt = new BigFileDecrypt();
	FileInfo fileInfo = new FileInfo();	//实例化一个文件读写对象
	FileZip fileZip = new FileZip();	//实例化一个解压缩对象
    private BasicHttpClient basicHttpClient = new BasicHttpClient();
    private ApacheHttpClient apacheHttpClient = new ApacheHttpClient();	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	/**
         * 2.3开始提供了一个新的类 StrictMode，该类可以用于捕捉发生在应用程序主线程中耗时的磁盘、
         * 网络访问或函数调用，可以帮助开发者改进程序，使主线程处理 UI 和动画在磁盘读写和网络操作时变得更平滑，
         * 避免主线程被阻塞。这是因为对网络连接的要求更严格了。
         */
        String strVer = android.os.Build.VERSION.RELEASE;	//获得当前系统的版本
        strVer = strVer.substring(0,3).trim();				//检出版本号
        float floatversion = Float.valueOf(strVer);
        if(floatversion > 2.3) {
        	 /*—————————————————————————Android3.0开始网络不能在主线程中—————————————*/
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
            /*—————————————————————————Android3.0开始网络不能在主线程中—————————————*/
        }
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_operation);
        
        findViews();			//查找相应控件
        setListeners();			//设置监听器
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_file_operation, menu);
        return true;
    }
    
  //查找相应控件
    public void findViews() {
    	btn_upload = (Button) this.findViewById(R.id.btn_upload);		//上传按钮
    	btn_download = (Button) this.findViewById(R.id.btn_download);	//下载按钮
    	btn_make_dir = (Button) this.findViewById(R.id.btn_make_dir);	//新建目录
    	btn_encryption = (Button) this.findViewById(R.id.btn_encryption);	//加密文件
    	btn_decryption = (Button) this.findViewById(R.id.btn_decryption);	//解密文件
    	btn_zip = (Button) this.findViewById(R.id.btn_zip);
    	btn_unzip = (Button) this.findViewById(R.id.btn_unzip);
    }
    
    //实现监听器
    public void setListeners() {
    	
    	//实现上传按钮监听器
    	btn_upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//新建一个Intent实例，用于设置Activity跳转
				Bundle bundle = new Bundle();										//新建一个Bundle用于设置Intent的数据
				bundle.putInt("FileOperation", upload);					//将键值对放在Bundle中
				intent.putExtras(bundle);					//将Bundle数据放在Intent中
				startActivityForResult(intent, upload);		//选择文件，上传
			}
		});
    	
    	//实现下载按钮监听器
    	btn_download.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//新建一个Intent实例，用于设置Activity跳转
				Bundle bundle = new Bundle();										//新建一个Bundle用于设置Intent的数据
				bundle.putInt("FileOperation", download);					//将键值对放在Bundle中
				intent.putExtras(bundle);					//将Bundle数据放在Intent中
				startActivityForResult(intent, download); 	//选择文件，下载	
			}
		});
    	
    	//实现新建目录监听器
    	btn_make_dir.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//新建一个Intent实例，用于设置Activity跳转
				Bundle bundle = new Bundle();										//新建一个Bundle用于设置Intent的数据
				bundle.putInt("FileOperation", make_dir);				//将键值对放在Bundle中
				intent.putExtras(bundle);					//将Bundle数据放在Intent中
				startActivityForResult(intent, make_dir); 	//选择文件，下载	
			}
		});
    	
    	//实现文件加密
    	btn_encryption.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//新建一个Intent实例，用于设置Activity跳转
				Bundle bundle = new Bundle();										//新建一个Bundle用于设置Intent的数据
				bundle.putInt("FileOperation", encryption);	//将键值对放在Bundle中
				intent.putExtras(bundle);					//将Bundle数据放在Intent中
				startActivityForResult(intent, encryption); //选择文件，下载
			}
		});
    	
    	//实现文件解密
    	btn_decryption.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//新建一个Intent实例，用于设置Activity跳转
				Bundle bundle = new Bundle();										//新建一个Bundle用于设置Intent的数据
				bundle.putInt("FileOperation", decryption);	//将键值对放在Bundle中
				intent.putExtras(bundle);					//将Bundle数据放在Intent中
				startActivityForResult(intent, decryption); //选择文件，下载
			}
		});
    	
    	//实现文件压缩
    	btn_zip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//新建一个Intent实例，用于设置Activity跳转
				Bundle bundle = new Bundle();										//新建一个Bundle用于设置Intent的数据
				bundle.putInt("FileOperation", zip);	//将键值对放在Bundle中
				intent.putExtras(bundle);				//将Bundle数据放在Intent中
				startActivityForResult(intent, zip); 	//选择文件，压缩
				
				
			}
		});
    	
    	//实现文件解压缩
    	btn_unzip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileOperation.this, FileExplorer.class);	//新建一个Intent实例，用于设置Activity跳转
				Bundle bundle = new Bundle();										//新建一个Bundle用于设置Intent的数据
				bundle.putInt("FileOperation", unzip);	//将键值对放在Bundle中
				intent.putExtras(bundle);				//将Bundle数据放在Intent中
				startActivityForResult(intent, unzip); 	//选择文件，压缩
			}
		});
    }

   
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if(upload == resultCode) {
			Log.i("result", "上传");
			fileName = data.getStringExtra("name");
			filePath = data.getStringExtra("file_path");
			Log.i("name", fileName);
			Log.i("file_path", filePath);
			try {
				SHA1 = digestMethod.byte2Str(digestMethod.SHA1(filePath));	//获取文件的SHA1值
				MD5 = digestMethod.byte2Str(digestMethod.MD5(filePath));	//获取文件的MD5值
				sizeB = digestMethod.fileSize(filePath);					//获取文件的大小
				
//				Log.i("FileSize", sizeB + "");								//输出文件的大小，单位为字节
				Log.i("fileSize", digestMethod.fileSize(filePath) + "");	//输出文件的大小，单位为字节
//				Log.i("getSize", digestMethod.getSize(filePath) + "");		//输出文件的大小，单位为字节
				Log.i("SHA1", SHA1);
				Log.i("MD5", MD5);
				
//				urlStr = urlBase + "access_token=" + CloudBackupActivity.access_token + "&oauth_consumer_key=" + CloudBackupActivity.appid 
//						+ "&openid=" + CloudBackupActivity.openid + "&sha=" + SHA1 + "&md5=" + MD5 + "&size=" + sizeB + "&name=" + charSet.toUTF_8(fileName);
				urlStr = urlBase + "access_token=" + CloudBackupActivity.access_token + "&oauth_consumer_key=" + CloudBackupActivity.appid + "&openid=" + CloudBackupActivity.openid;
				System.out.println("urlStr:" + urlStr);
				System.out.println("BasicHttpClient结果：" + basicHttpClient.httpGet(urlStr));
				System.out.println("ApacheHttpClient结果：" + apacheHttpClient.httpGet(urlStr));
				
				/**
				 * 实现HttpGet方式
				 */
				/*HttpClient httpClient = new DefaultHttpClient();	//创建一个HttpClient对象
				HttpGet httpGet = new HttpGet(urlStr);
				System.out.println("httpGet执行");
				HttpResponse httpResponse = httpClient.execute(httpGet);	//调用execute()方法发送请求并返回相应
				HttpEntity entity = httpResponse.getEntity();	//HttpClient将响应内容封装为HttpClient
				int statusCode = httpResponse.getStatusLine().getStatusCode();	//获得状态码
				System.out.println("响应码：" + statusCode);*/
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
			Log.i("result", "下载");
			fileName = data.getStringExtra("name");
			filePath = data.getStringExtra("file_path");
			Log.i("name", fileName);
			Log.i("file_path", filePath);
				
		} else if(make_dir == resultCode) {
			Log.i("result", "新建目录");
		} else if(encryption == resultCode) {
			Log.i("result", "加密文件");
			fileName = data.getStringExtra("name");
			filePath = data.getStringExtra("file_path");
//			bigFileDecrypt.enCrByteFile(filePath, filePath, strKey);
			fileDEncryption.encrypt(filePath, filePath, strKey, alg);
//			fileInfo.writeFile(filePath, fileDEncryption.encrypt(filePath, strKey, "DES"));
			Log.i("加密目录", filePath);
		} else if(decryption == requestCode) {
			Log.i("result", "解密文件");
			fileName = data.getStringExtra("name");
			filePath = data.getStringExtra("file_path");
//			bigFileDecrypt.deCrByteFile(filePath, filePath, strKey);
			fileDEncryption.decrypt(filePath, filePath, strKey, alg);
//			fileInfo.writeFile(filePath, fileDEncryption.decrypt(filePath, strKey, "DES"));
			Log.i("解密目录", filePath);
		} else if(zip == resultCode) {
			Log.i("result", "压缩");
			fileName = data.getStringExtra("name");
			filePath = data.getStringExtra("file_path");
			
			Thread thread = new Thread(new Runnable(){	//创建新的线程用于压缩文件
				public void run() {
					FileZ4J filez4j = new FileZ4J();
					filez4j.zip(filePath, filePath + ".zip", false, "");
					}
				}); // 创建新线程
			thread.start(); // 开启线程
			
			
			Log.i("name", fileName);
			Log.i("file_path", filePath);
		} else if(unzip == resultCode) {
			Log.i("result", "解压缩");
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
