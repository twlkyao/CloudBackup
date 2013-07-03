/**
 * 作者：			齐士垚
 * 日期：	 		2013.6.13
 * 功能：			用ListView实现文件资源管理，并且有返回根目录很上一层目录的选项，可以实现对新建目录，文件的上传，下载，压缩，解压缩，加密，解密
 * 问题：			现在根目录和上层目录有问题，而且空目录有问题，已完成（加密）压缩，（带密）解压缩，
 */	

package com.twlkyao.cloudbackup;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lingala.zip4j.exception.ZipException;

import com.twlkyao.utils.FileZ4J;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FileExplorer extends Activity {
	
	private ListView listview;			//声明一个ListView实例，用于存储文件信息
	private SimpleAdapter simpleAdapter;	//声明一个适配器，用于将数据映射到ListView中
//	private List<Map<String, Object>> filelist = null;	//用于存储文件数据信息
	private String root = Environment.getExternalStorageDirectory().toString();	//用于存储系统的SD卡根目录
	private TextView fileName;	//用于存储文件名
	private TextView filePath;	//用于存储文件路径
	private FileZ4J fileZ4J = new FileZ4J();	//新建FileZ4J实例，用于解压缩文件
	
	private ProgressDialog progressDialog;	//进度对话框，用于对用户进行提示
	private Intent intent;				//声明Intent，用于设置Activity跳转
	
	private final int upload = 0;		//用于表示startActivityforResult的requestCode
	private final int download = 1;		//用于表示startActivityforResult的requestCode
	private final int makedir = 2;		//用于表示startActivityforResult的requestCode
	private final int encrypt = 3;		//用于表示startActivityforResult的requestCode
	private final int decrypt = 4;		//用于表示startActivityforResult的requestCode
	private final int compress = 5;		//用于表示startActivityforResult的requestCode
	private final int unCompress = 6;	//用于表示startActivityforResult的requestCode
	
	
	private String strFileName;	//用于记录文件名称
	private String strFilePath;	//用于记录文件路径
	private String strPasswd;	//用于记录密码
	private String strDestFilePath;	//用于记录目标文件名
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fileexplorer);	//设置页面布局
		findViews();	//查找到相应的视图控件
		
//		filelist = getData(root);	//获得文件信息数据，存储在filelist中
		getData(root);	//获得文件信息数据，存储在filelist中
//		simpleAdapter = new SimpleAdapter(this, filelist, R.layout.filelist,
//				new String[] { "icon", "name", "path"}, new int[] { R.id.icon, R.id.name,
//						R.id.path});	//新建一个Adapter
//		listview.setAdapter(simpleAdapter);	//利用Adapter将listview和数据适配
		
		setListeners();	//设置监听器
	}

	 /** 
     * 用Handler来更新UI 
     */  
    private Handler handler = new Handler(){  
  
        @Override  
        public void handleMessage(Message msg) {  
              
            //关闭ProgressDialog  
            progressDialog.dismiss();
        }};  
	
	public void findViews() {
		listview = (ListView) this.findViewById(R.id.listview);
//		relativeLayout = (RelativeLayout) this.findViewById(R.id.layout_passwd);
//		editTextPasswd = (EditText) this.findViewById(R.id.editText_passwd);
//		btn_positive = (Button) this.findViewById(R.id.btn_positive);
//		btn_reset = (Button) this.findViewById(R.id.btn_reset);
//		btn_auto = (Button) this.findViewById(R.id.btn_auto);
	}
	
	/**
	 * 功能：	根据给定的文件路径，获取该路径下的文件信息
	 * @param filePath 给定的文件路径
	 * @return 给定文件路径filePath下的文件信息
	 */
//	public List<Map<String, Object>> getData(String filePath) {
	public void getData(String filePath) {
		
		
		List<Map<String, Object>> filelists = new ArrayList<Map<String, Object>>();	//新建一个List用于获取最后的文件列表
		filelists.clear();					//清空filelist
		File file = new File(filePath);		//根据给定的文件路径，获取该路径下的文件信息
		Map<String, Object> map = new HashMap<String, Object>();	//新建一个map实例，用于存储数据
		if(root != filePath) {
			/*向filelist中添加返回根目录选项*/
			map.put("icon", R.drawable.root);	//向map中添加根目录的图标
	   		map.put("name", "返回根目录");	//向map中添加根目录的名字
	  		map.put("path", root);		//向map中添加根目录的路径信息
	  		filelists.add(map);			//向filelist中添加数据
	  		
	  		/*向filelist中添加返回上层目录选项*/
	  		map = new HashMap<String, Object>();	//新建一个map实例，用于存储数据
	  		map.put("icon", R.drawable.uplevel);	//向map中添加返回上层目录的图标
	  		map.put("name", "返回父目录");	//向map中添加返回上层目录的名字
	  		map.put("path", file.getParent().toString());	//向map中添加上层目录的路径信息
	  		filelists.add(map);
		}
		
  		/*向filelist中添加给定目录下的文件信息*/
  		File [] listfiles = file.listFiles();
  		
  		if(listfiles != null) {	//如果路径不是目录
  			for(int i = 0; i < listfiles.length; i++) {
  				map = new HashMap<String, Object>();	//实例化一个map实例，用于存储数据
  				if(listfiles[i].isDirectory()) {		//如果路径是目录
  					if(null != listfiles[i].listFiles()) {	//如果目录非空，则添加文件夹图标
  						map.put("icon", R.drawable.folder);
  					} else if(null == listfiles[i].listFiles()) {	//如果目录为空，则添加空目录图标
  	  					map.put("icon", R.drawable.empty);
  	  				}
  				} else if(listfiles[i].isFile()) {	//如果是文件，则添加文件图标
  					map.put("icon", R.drawable.file);
  				}
  				map.put("name", listfiles[i].getName());	//添加文件名
  				map.put("path", listfiles[i].getPath());	//添加文件路径
  				filelists.add(map);							//将数据添加到filelist中
  			}
  		}
  		
  		simpleAdapter = new SimpleAdapter(this, filelists, R.layout.filelist,
				new String[] { "icon", "name", "path"}, new int[] { R.id.icon, R.id.name,
						R.id.path});	//新建一个Adapter
		listview.setAdapter(simpleAdapter);	//利用Adapter将listview和数据适配
//		return filelists;
	}
	
	/**
	 * 功能：	设置ListView条目的点击和长按监听器。
	 */
	public void setListeners() {
		
		/**
		 * 功能：	设置ListView的点击事件，点击的如果是文件，则返回，如果是文件目录，则进入目录，获取文件，
		 * 		如果是返回根目录，则返回根目录，如果是返回上层目录，则返回上层目录。
		 */
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				Log.i("postion", position + "");
				Log.i("id", id + "");
				
				fileName = (TextView) view.findViewById(R.id.name);
    			filePath = (TextView) view.findViewById(R.id.path);
    			
    			String strFileName = fileName.getText().toString();
    			String strFilePath = filePath.getText().toString();
    			
    			Log.i("itemName", strFileName);
        		Log.i("itempath", strFilePath);
        		
        		File file = new File(strFilePath);	//新建一个文件，用于判断是目录还是文件
//        		if("返回根目录" == strFileName) {	//点击的返回根目录
        		if(0 == position) {	//点击的返回根目录
//        			filelist = getData(root);	//获得文件信息数据，存储在filelist中
        			getData(root);	//获得文件信息数据，存储在filelist中
        		} else if(1 == position) {		//点击的返回父目录
//        			filelist = getData(strFilePath);	//获得文件信息数据，存储在filelist中
//        			if(!strFilePath.equals("storage"))
        			getData(strFilePath);	//获得文件信息数据，存储在filelist中
        		} else if(file.isDirectory() && null != file.listFiles()){	//点击的是一般的目录，并且非空
//        			filelist = getData(strFilePath);
        			getData(strFilePath);
        		} else if(null == file.listFiles()){	//点击的是空目录
        			Toast.makeText(getApplicationContext(), "空目录", Toast.LENGTH_SHORT).show();
        		} else if(file.isFile()) {		//点击的是文件
        			Log.i("fileName", strFileName);
        			Log.i("filePath", strFilePath);
        		}
			}
		});
		
		/**
		 * 功能：	长按条目，则弹出上下文菜单，菜单项包括：上传，下载，新建目录，加密，解密，压缩，解压缩。
		 */
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				fileName = (TextView) view.findViewById(R.id.name);
    			filePath = (TextView) view.findViewById(R.id.path);
    			
    			strFileName = fileName.getText().toString();
    			strFilePath = filePath.getText().toString();
    			
        		Log.i("itemName", strFileName);
        		Log.i("itempath", strFilePath);
				
//        		fileZ4J.zip(strFilePath, strFilePath, false, "");
        		
				new AlertDialog.Builder(FileExplorer.this)
					.setTitle("选择对文件进行操作")
					.setItems(R.array.choices, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {	//AlertDialog点击处理事件
							switch(which) {
							case upload:	//上传文件
//								relativeLayout.setVisibility(View.VISIBLE);			//设置密码输入框可见
								//显示ProgressDialog  
//								progressDialog = ProgressDialog.show(FileExplorer.this, "上传文件", "请稍后……", true, false);
//								upload(String filePath, String addresss);			//给出上传文件的路径，目的地址，完成上传
//								progressDialog.dismiss();							//文件上传完成后，关闭进度条对话框
								break;
							case download:	//下载文件
//								progressDialog.setTitle("正在下载，请稍候……");			//显示进度对话框，对用户进行提示
//								download(String remoteFilePath, String filePath);	//下载远端指定目录文件到本地指定目录
//								progressDialog.dismiss();							//文件上传完成后，关闭进度条对话框
								break;
							case makedir:	//新建目录
								Toast.makeText(getApplicationContext(), "创建目录", Toast.LENGTH_SHORT).show();
								break;
							case encrypt:	//加密文件
//								relativeLayout.setVisibility(View.VISIBLE);			//设置密码输入框可见
								
								Toast.makeText(getApplicationContext(), "加密", Toast.LENGTH_SHORT).show();
								break;
							case decrypt:	//解密文件
//								relativeLayout.setVisibility(View.VISIBLE);			//设置密码输入框可见
								Toast.makeText(getApplicationContext(), "解密", Toast.LENGTH_SHORT).show();
								break;
							case compress:	//压缩文件
								/*relativeLayout.setVisibility(View.VISIBLE);			//设置密码输入框可见
								progressDialog = ProgressDialog.show(FileExplorer.this, "压缩文件", "请稍后……", true, false);
								 //新建线程用于压缩文件
				                new Thread(){  
				  
				                    @Override  
				                    public void run() {  
				                        //需要花时间计算的方法  
				                    	fileZ4J.zip(strFilePath, strFilePath + ".zip", false, strPasswd);	//对文件进行压缩
				                          
				                        //向handler发消息  
				                        handler.sendEmptyMessage(0);
				                    }}.start();  */
								intent = new Intent(FileExplorer.this, Passwd.class);
								Bundle bundle = new Bundle();										//新建一个Bundle用于设置Intent的数据
								bundle.putInt("FileExplorer", compress);	//将键值对放在Bundle中
								intent.putExtras(bundle);					//将Bundle数据放在Intent中
								startActivityForResult(intent, compress); 	//选择文件，压缩
								break;
							case unCompress:	//解压缩文件
//								relativeLayout.setVisibility(View.VISIBLE);			//设置密码输入框可见
//								progressDialog = ProgressDialog.show(FileExplorer.this, "解压文件", "请稍后……", true, false);
								 //新建线程用于压缩文件
//				                new Thread(){  
//				  
//				                    @Override  
//				                    public void run() {  
//				                        //需要花时间计算的方法  
//				                    	try {
//											fileZ4J.unzip(strFilePath, strPasswd);
//										} catch (ZipException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										}		//对文件进行解压
//				                          
//				                        //向handler发消息  
//				                        handler.sendEmptyMessage(0);
//				                    }}.start();
								
								intent = new Intent(FileExplorer.this, Passwd.class);		//新建一个intent用于跳转
								bundle = new Bundle();										//新建一个Bundle用于设置Intent的数据
								bundle.putInt("FileExplorer", unCompress);	//将键值对放在Bundle中
								intent.putExtras(bundle);					//将Bundle数据放在Intent中
								startActivityForResult(intent, unCompress); 	//选择文件，压缩
								break;
							default:
									break;
							}
 						}}).show();
				return false;
			}
			
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(resultCode) {
		case upload:
			break;
		case compress:
			Log.i("result", "压缩");
			strPasswd = data.getStringExtra("passwd");
			strDestFilePath = strFilePath + ".zip";		//用于记录压缩后的文件名
			progressDialog = ProgressDialog.show(FileExplorer.this, "压缩文件", "请稍后……", true, false);
			//新建线程用于压缩文件
			new Thread(){  

				@Override  
				public void run() {  
					//需要花时间计算的方法  
					fileZ4J.zip(strFilePath, strDestFilePath, false, strPasswd);	//对文件进行压缩
					//向handler发消息  
					handler.sendEmptyMessage(0);
				}
			}.start();
			break;
		case unCompress:
			Log.i("result", "解压缩");
			strPasswd = data.getStringExtra("passwd");
			strDestFilePath = strFilePath.substring(0, strFilePath.indexOf("."));	//用于记录解压缩后的文件名
			progressDialog = ProgressDialog.show(FileExplorer.this, "解压缩文件", "请稍后……", true, false);
			//新建线程用于压缩文件
			new Thread(){  
				@Override  
				public void run() {  
					//需要花时间计算的方法  
					try {
//						fileZ4J.unzip(strFilePath, strPasswd);	//解压缩文件
						fileZ4J.unzip(strFilePath, strDestFilePath, strPasswd);	//对压缩文件进行解压
					} catch (ZipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//向handler发消息  
					handler.sendEmptyMessage(0);
				}
			}.start();
			break;
		case encrypt:
			Log.i("result", "加密");
			break;
		}
	}
	
	
}
