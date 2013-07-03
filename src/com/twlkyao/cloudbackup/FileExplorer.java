/**
 * ���ߣ�			��ʿ��
 * ���ڣ�	 		2013.6.13
 * ���ܣ�			��ListViewʵ���ļ���Դ���������з��ظ�Ŀ¼����һ��Ŀ¼��ѡ�����ʵ�ֶ��½�Ŀ¼���ļ����ϴ������أ�ѹ������ѹ�������ܣ�����
 * ���⣺			���ڸ�Ŀ¼���ϲ�Ŀ¼�����⣬���ҿ�Ŀ¼�����⣬����ɣ����ܣ�ѹ���������ܣ���ѹ����
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
	
	private ListView listview;			//����һ��ListViewʵ�������ڴ洢�ļ���Ϣ
	private SimpleAdapter simpleAdapter;	//����һ�������������ڽ�����ӳ�䵽ListView��
//	private List<Map<String, Object>> filelist = null;	//���ڴ洢�ļ�������Ϣ
	private String root = Environment.getExternalStorageDirectory().toString();	//���ڴ洢ϵͳ��SD����Ŀ¼
	private TextView fileName;	//���ڴ洢�ļ���
	private TextView filePath;	//���ڴ洢�ļ�·��
	private FileZ4J fileZ4J = new FileZ4J();	//�½�FileZ4Jʵ�������ڽ�ѹ���ļ�
	
	private ProgressDialog progressDialog;	//���ȶԻ������ڶ��û�������ʾ
	private Intent intent;				//����Intent����������Activity��ת
	
	private final int upload = 0;		//���ڱ�ʾstartActivityforResult��requestCode
	private final int download = 1;		//���ڱ�ʾstartActivityforResult��requestCode
	private final int makedir = 2;		//���ڱ�ʾstartActivityforResult��requestCode
	private final int encrypt = 3;		//���ڱ�ʾstartActivityforResult��requestCode
	private final int decrypt = 4;		//���ڱ�ʾstartActivityforResult��requestCode
	private final int compress = 5;		//���ڱ�ʾstartActivityforResult��requestCode
	private final int unCompress = 6;	//���ڱ�ʾstartActivityforResult��requestCode
	
	
	private String strFileName;	//���ڼ�¼�ļ�����
	private String strFilePath;	//���ڼ�¼�ļ�·��
	private String strPasswd;	//���ڼ�¼����
	private String strDestFilePath;	//���ڼ�¼Ŀ���ļ���
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fileexplorer);	//����ҳ�沼��
		findViews();	//���ҵ���Ӧ����ͼ�ؼ�
		
//		filelist = getData(root);	//����ļ���Ϣ���ݣ��洢��filelist��
		getData(root);	//����ļ���Ϣ���ݣ��洢��filelist��
//		simpleAdapter = new SimpleAdapter(this, filelist, R.layout.filelist,
//				new String[] { "icon", "name", "path"}, new int[] { R.id.icon, R.id.name,
//						R.id.path});	//�½�һ��Adapter
//		listview.setAdapter(simpleAdapter);	//����Adapter��listview����������
		
		setListeners();	//���ü�����
	}

	 /** 
     * ��Handler������UI 
     */  
    private Handler handler = new Handler(){  
  
        @Override  
        public void handleMessage(Message msg) {  
              
            //�ر�ProgressDialog  
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
	 * ���ܣ�	���ݸ������ļ�·������ȡ��·���µ��ļ���Ϣ
	 * @param filePath �������ļ�·��
	 * @return �����ļ�·��filePath�µ��ļ���Ϣ
	 */
//	public List<Map<String, Object>> getData(String filePath) {
	public void getData(String filePath) {
		
		
		List<Map<String, Object>> filelists = new ArrayList<Map<String, Object>>();	//�½�һ��List���ڻ�ȡ�����ļ��б�
		filelists.clear();					//���filelist
		File file = new File(filePath);		//���ݸ������ļ�·������ȡ��·���µ��ļ���Ϣ
		Map<String, Object> map = new HashMap<String, Object>();	//�½�һ��mapʵ�������ڴ洢����
		if(root != filePath) {
			/*��filelist����ӷ��ظ�Ŀ¼ѡ��*/
			map.put("icon", R.drawable.root);	//��map����Ӹ�Ŀ¼��ͼ��
	   		map.put("name", "���ظ�Ŀ¼");	//��map����Ӹ�Ŀ¼������
	  		map.put("path", root);		//��map����Ӹ�Ŀ¼��·����Ϣ
	  		filelists.add(map);			//��filelist���������
	  		
	  		/*��filelist����ӷ����ϲ�Ŀ¼ѡ��*/
	  		map = new HashMap<String, Object>();	//�½�һ��mapʵ�������ڴ洢����
	  		map.put("icon", R.drawable.uplevel);	//��map����ӷ����ϲ�Ŀ¼��ͼ��
	  		map.put("name", "���ظ�Ŀ¼");	//��map����ӷ����ϲ�Ŀ¼������
	  		map.put("path", file.getParent().toString());	//��map������ϲ�Ŀ¼��·����Ϣ
	  		filelists.add(map);
		}
		
  		/*��filelist����Ӹ���Ŀ¼�µ��ļ���Ϣ*/
  		File [] listfiles = file.listFiles();
  		
  		if(listfiles != null) {	//���·������Ŀ¼
  			for(int i = 0; i < listfiles.length; i++) {
  				map = new HashMap<String, Object>();	//ʵ����һ��mapʵ�������ڴ洢����
  				if(listfiles[i].isDirectory()) {		//���·����Ŀ¼
  					if(null != listfiles[i].listFiles()) {	//���Ŀ¼�ǿգ�������ļ���ͼ��
  						map.put("icon", R.drawable.folder);
  					} else if(null == listfiles[i].listFiles()) {	//���Ŀ¼Ϊ�գ�����ӿ�Ŀ¼ͼ��
  	  					map.put("icon", R.drawable.empty);
  	  				}
  				} else if(listfiles[i].isFile()) {	//������ļ���������ļ�ͼ��
  					map.put("icon", R.drawable.file);
  				}
  				map.put("name", listfiles[i].getName());	//����ļ���
  				map.put("path", listfiles[i].getPath());	//����ļ�·��
  				filelists.add(map);							//��������ӵ�filelist��
  			}
  		}
  		
  		simpleAdapter = new SimpleAdapter(this, filelists, R.layout.filelist,
				new String[] { "icon", "name", "path"}, new int[] { R.id.icon, R.id.name,
						R.id.path});	//�½�һ��Adapter
		listview.setAdapter(simpleAdapter);	//����Adapter��listview����������
//		return filelists;
	}
	
	/**
	 * ���ܣ�	����ListView��Ŀ�ĵ���ͳ�����������
	 */
	public void setListeners() {
		
		/**
		 * ���ܣ�	����ListView�ĵ���¼��������������ļ����򷵻أ�������ļ�Ŀ¼�������Ŀ¼����ȡ�ļ���
		 * 		����Ƿ��ظ�Ŀ¼���򷵻ظ�Ŀ¼������Ƿ����ϲ�Ŀ¼���򷵻��ϲ�Ŀ¼��
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
        		
        		File file = new File(strFilePath);	//�½�һ���ļ��������ж���Ŀ¼�����ļ�
//        		if("���ظ�Ŀ¼" == strFileName) {	//����ķ��ظ�Ŀ¼
        		if(0 == position) {	//����ķ��ظ�Ŀ¼
//        			filelist = getData(root);	//����ļ���Ϣ���ݣ��洢��filelist��
        			getData(root);	//����ļ���Ϣ���ݣ��洢��filelist��
        		} else if(1 == position) {		//����ķ��ظ�Ŀ¼
//        			filelist = getData(strFilePath);	//����ļ���Ϣ���ݣ��洢��filelist��
//        			if(!strFilePath.equals("storage"))
        			getData(strFilePath);	//����ļ���Ϣ���ݣ��洢��filelist��
        		} else if(file.isDirectory() && null != file.listFiles()){	//�������һ���Ŀ¼�����ҷǿ�
//        			filelist = getData(strFilePath);
        			getData(strFilePath);
        		} else if(null == file.listFiles()){	//������ǿ�Ŀ¼
        			Toast.makeText(getApplicationContext(), "��Ŀ¼", Toast.LENGTH_SHORT).show();
        		} else if(file.isFile()) {		//��������ļ�
        			Log.i("fileName", strFileName);
        			Log.i("filePath", strFilePath);
        		}
			}
		});
		
		/**
		 * ���ܣ�	������Ŀ���򵯳������Ĳ˵����˵���������ϴ������أ��½�Ŀ¼�����ܣ����ܣ�ѹ������ѹ����
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
					.setTitle("ѡ����ļ����в���")
					.setItems(R.array.choices, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {	//AlertDialog��������¼�
							switch(which) {
							case upload:	//�ϴ��ļ�
//								relativeLayout.setVisibility(View.VISIBLE);			//�������������ɼ�
								//��ʾProgressDialog  
//								progressDialog = ProgressDialog.show(FileExplorer.this, "�ϴ��ļ�", "���Ժ󡭡�", true, false);
//								upload(String filePath, String addresss);			//�����ϴ��ļ���·����Ŀ�ĵ�ַ������ϴ�
//								progressDialog.dismiss();							//�ļ��ϴ���ɺ󣬹رս������Ի���
								break;
							case download:	//�����ļ�
//								progressDialog.setTitle("�������أ����Ժ򡭡�");			//��ʾ���ȶԻ��򣬶��û�������ʾ
//								download(String remoteFilePath, String filePath);	//����Զ��ָ��Ŀ¼�ļ�������ָ��Ŀ¼
//								progressDialog.dismiss();							//�ļ��ϴ���ɺ󣬹رս������Ի���
								break;
							case makedir:	//�½�Ŀ¼
								Toast.makeText(getApplicationContext(), "����Ŀ¼", Toast.LENGTH_SHORT).show();
								break;
							case encrypt:	//�����ļ�
//								relativeLayout.setVisibility(View.VISIBLE);			//�������������ɼ�
								
								Toast.makeText(getApplicationContext(), "����", Toast.LENGTH_SHORT).show();
								break;
							case decrypt:	//�����ļ�
//								relativeLayout.setVisibility(View.VISIBLE);			//�������������ɼ�
								Toast.makeText(getApplicationContext(), "����", Toast.LENGTH_SHORT).show();
								break;
							case compress:	//ѹ���ļ�
								/*relativeLayout.setVisibility(View.VISIBLE);			//�������������ɼ�
								progressDialog = ProgressDialog.show(FileExplorer.this, "ѹ���ļ�", "���Ժ󡭡�", true, false);
								 //�½��߳�����ѹ���ļ�
				                new Thread(){  
				  
				                    @Override  
				                    public void run() {  
				                        //��Ҫ��ʱ�����ķ���  
				                    	fileZ4J.zip(strFilePath, strFilePath + ".zip", false, strPasswd);	//���ļ�����ѹ��
				                          
				                        //��handler����Ϣ  
				                        handler.sendEmptyMessage(0);
				                    }}.start();  */
								intent = new Intent(FileExplorer.this, Passwd.class);
								Bundle bundle = new Bundle();										//�½�һ��Bundle��������Intent������
								bundle.putInt("FileExplorer", compress);	//����ֵ�Է���Bundle��
								intent.putExtras(bundle);					//��Bundle���ݷ���Intent��
								startActivityForResult(intent, compress); 	//ѡ���ļ���ѹ��
								break;
							case unCompress:	//��ѹ���ļ�
//								relativeLayout.setVisibility(View.VISIBLE);			//�������������ɼ�
//								progressDialog = ProgressDialog.show(FileExplorer.this, "��ѹ�ļ�", "���Ժ󡭡�", true, false);
								 //�½��߳�����ѹ���ļ�
//				                new Thread(){  
//				  
//				                    @Override  
//				                    public void run() {  
//				                        //��Ҫ��ʱ�����ķ���  
//				                    	try {
//											fileZ4J.unzip(strFilePath, strPasswd);
//										} catch (ZipException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										}		//���ļ����н�ѹ
//				                          
//				                        //��handler����Ϣ  
//				                        handler.sendEmptyMessage(0);
//				                    }}.start();
								
								intent = new Intent(FileExplorer.this, Passwd.class);		//�½�һ��intent������ת
								bundle = new Bundle();										//�½�һ��Bundle��������Intent������
								bundle.putInt("FileExplorer", unCompress);	//����ֵ�Է���Bundle��
								intent.putExtras(bundle);					//��Bundle���ݷ���Intent��
								startActivityForResult(intent, unCompress); 	//ѡ���ļ���ѹ��
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
			Log.i("result", "ѹ��");
			strPasswd = data.getStringExtra("passwd");
			strDestFilePath = strFilePath + ".zip";		//���ڼ�¼ѹ������ļ���
			progressDialog = ProgressDialog.show(FileExplorer.this, "ѹ���ļ�", "���Ժ󡭡�", true, false);
			//�½��߳�����ѹ���ļ�
			new Thread(){  

				@Override  
				public void run() {  
					//��Ҫ��ʱ�����ķ���  
					fileZ4J.zip(strFilePath, strDestFilePath, false, strPasswd);	//���ļ�����ѹ��
					//��handler����Ϣ  
					handler.sendEmptyMessage(0);
				}
			}.start();
			break;
		case unCompress:
			Log.i("result", "��ѹ��");
			strPasswd = data.getStringExtra("passwd");
			strDestFilePath = strFilePath.substring(0, strFilePath.indexOf("."));	//���ڼ�¼��ѹ������ļ���
			progressDialog = ProgressDialog.show(FileExplorer.this, "��ѹ���ļ�", "���Ժ󡭡�", true, false);
			//�½��߳�����ѹ���ļ�
			new Thread(){  
				@Override  
				public void run() {  
					//��Ҫ��ʱ�����ķ���  
					try {
//						fileZ4J.unzip(strFilePath, strPasswd);	//��ѹ���ļ�
						fileZ4J.unzip(strFilePath, strDestFilePath, strPasswd);	//��ѹ���ļ����н�ѹ
					} catch (ZipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//��handler����Ϣ  
					handler.sendEmptyMessage(0);
				}
			}.start();
			break;
		case encrypt:
			Log.i("result", "����");
			break;
		}
	}
	
	
}
