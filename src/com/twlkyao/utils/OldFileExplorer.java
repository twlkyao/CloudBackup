package com.twlkyao.utils;

/**
 * ���ߣ�		����
 * �޸��ߣ�	��ʿ��
 * ���ڣ�		2013.5.10
 * ���ܣ�		�ļ�������
 * ���Ż���	���ڡ������ϲ�Ŀ¼���͡����ظ�Ŀ¼��һֱ����
 */


import android.os.Bundle;
import android.os.Environment;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.app.ListActivity;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;

import com.twlkyao.cloudbackup.R;


public class OldFileExplorer extends ListActivity {
	private List<Map<String, Object>> fileList;	//���ڴ洢�ļ���Ϣ
	private SimpleAdapter simpleAdapter;	//����list��listview֮�������
	private ListView listview = null;		//ListView����
	private static int i = 1;
	private String sdCardFilePath = Environment.getExternalStorageDirectory().toString();	//��Ŀ¼
	public static int upload = 1;		//�ϴ��ļ�������
	public static int download = 2;		//�����ļ�������
	public static int make_dir = 3;		//�½�Ŀ¼������
	String resultCode;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*findViews();
		refreshListView();
		setListeners();*/
		
		fileList = getList(sdCardFilePath);				//����ļ����б�
		MyAdapter myAdapter = new MyAdapter(this);		//ʵ����һ���µ�������
		setListAdapter(myAdapter);						//����������
		
		Log.i("��������", "onCreate");
	}
	
	
	/*public void findViews() {
		listview = (ListView) this.findViewById(R.id.file_listview);
	}*/
	
	public void setListeners() {
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		listview.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	/*public void refreshListView() {
		
		fileList = getList(sdCardFilePath);		//�����Զ��庯������ð�װ������б�
		simpleAdapter = new SimpleAdapter(this, fileList, R.layout.file_info_row,
				new String[] { "icon", "name", "file_path" }, new int[] { R.id.icon, R.id.name, R.id.file_path });		//�½�һ��Adapter
		listview.setAdapter(simpleAdapter);		//����Adapter
	}
	*/
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i("��������", "onPause");
		super.onPause();
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.i("��������", "onRestart");
		super.onRestart();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i("��������", "onResume");
		super.onResume();
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i("��������", "onStart");
		super.onStart();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i("��������", "onStop");
		super.onStop();
	}


	/**
	 * @function ����filePathĿ¼�µ��ļ���Ϣ
	 * @param filePath �ļ�·����
	 * @return �ļ���Ϣ�б�
	 */
	private List<Map<String, Object>> getList(String currentfilePath) {	
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();	//�½�һ��List���ڻ�ȡ�����ļ��б�
		Map<String, Object> map = null;			//����һ��Mapʵ�������ڴ洢����
		File file = new File(currentfilePath);			//����filePathĿ¼����һ���µ��ļ�
		File[] files = file.listFiles();		//���ص�ǰ�ļ�Ŀ¼�µ��ļ��б�
		
		//������һ��Ŀ¼
//		if (!file.equals(Environment.getExternalStorageDirectory())) {		//���û�дﵽSD����Ŀ¼
		if (file.getPath() != sdCardFilePath) {
			//���SD����Ŀ¼
			map = new HashMap<String,Object>();		//ʵ����map
			map.put("icon", R.drawable.root);	//���ø�Ŀ¼ͼ��
			map.put("name", "����SD����Ŀ¼");			//���÷��ظ�Ŀ¼��
//			map.put("file_path", Environment.getExternalStorageDirectory().toString());			//���÷��ظ�Ŀ¼·��
			map.put("file_path", sdCardFilePath);
			list.add(map);						//��������ӵ�list��
			
			Log.i("SD��Ŀ¼", file.getPath());
			/*Log.i(i + "Ŀ¼", filePath);
			i ++;*/
			
			map = new HashMap<String, Object>();//ʵ����map
			map.put("icon", R.drawable.uplevel);//����ͼ��
			map.put("name", "�����ϲ�Ŀ¼");		//���ñ���
			map.put("file_path", file.getParent());	//������ϸ��Ϣ����õ�ǰ�ļ��ĸ�Ŀ¼��·��
			list.add(map);						//��������ӵ�list��
			
			Log.i("���� ��ǰĿ¼", file.getPath());
			Log.i("���� ��Ŀ¼", file.getParent());
			Log.i("���� ��ǰSdcardĿ¼", sdCardFilePath);
			
		} 
		
//		if(file.getParent() != sdCardFilePath && file.getPath() != sdCardFilePath && file.getPath() != "/" && file.getParent() != "/mnt/") {			//���δ����ָ��Ŀ¼������ӷ����ϲ�Ŀ¼
		/*	
		if(file.getPath() != sdCardFilePath && file.getParent() != sdCardFilePath) {//��ӷ����ϲ�Ŀ¼
			map = new HashMap<String, Object>();//ʵ����map
			map.put("icon", R.drawable.file_up);//����ͼ��
			map.put("name", "�����ϲ�Ŀ¼");		//���ñ���
			map.put("file_path", file.getParent());	//������ϸ��Ϣ����õ�ǰ�ļ��ĸ�Ŀ¼��·��
			list.add(map);						//��������ӵ�list��
			
			Log.i("���� ��ǰĿ¼", file.getPath());
			Log.i("���� ��Ŀ¼", file.getParent());
			Log.i("���� ��ǰSdcardĿ¼", sdCardFilePath);
			
//			Log.i(i + "Ŀ¼", filePath);
//			i ++;
		}*/
		
		//һ���Ŀ¼�����ļ�
		if(files != null) {	//�Ǹ�Ŀ¼��������Ŀ¼�ļ�
			for (int i = 0; i < files.length; i++) {	//����Ŀ¼�����е��ļ���Ŀ¼
				map = new HashMap<String, Object>();	//ʵ����map
				if (files[i].isDirectory())				//�����Ŀ¼������Ŀ¼ͼ��
					map.put("icon", R.drawable.folder);
				else if(files[i].isFile())				//������ļ��������ļ�ͼ��
					map.put("icon", R.drawable.file);
				map.put("name", files[i].getName());	//����ļ�����Ŀ¼��
				map.put("file_path", files[i].getPath());	//�õ��ļ�����Ŀ¼��·��
				
				list.add(map);							//��ListView���������
				
//				Log.i("file.getPath()", file.getPath());
				
//				Log.i(i + "Ŀ¼", filePath);
//				i ++;
			}
		}
		if(null == files) {	//��Ŀ¼
			map = new HashMap<String, Object>();
			map.put("icon", R.drawable.empty);
			map.put("name", "��Ŀ¼");
			map.put("file_path", "");
			list.add(map);
			
//			Log.i("file.getPath()", file.getPath());
			
			/*Log.i(i + "Ŀ¼", filePath);
			i ++;*/
			
		}
		setTitle(currentfilePath);		//���ñ���
		return list;
	}

	
	
	@Override
	public long getSelectedItemId() {
		// TODO Auto-generated method stub
		return super.getSelectedItemId();
	}

	@Override
	public int getSelectedItemPosition() {
		// TODO Auto-generated method stub
		return super.getSelectedItemPosition();
	}

	@Override
	public void onListItemClick(ListView listview, View view, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(listview, view, position, id);
		String name = fileList.get(position).get("name").toString();
		String filePath = fileList.get(position).get("file_path").toString();
		
		File newFile = new File(filePath);
		if(newFile.isDirectory()){	//�����������ļ�Ŀ¼������ļ�Ŀ¼
				
			Log.i("name", name);
			fileList = getList(newFile.getPath());		//����ļ����б�
			MyAdapter myAdapter = new MyAdapter(this);	//ʵ����һ���µ�������
			setListAdapter(myAdapter);		//����������
		} 
		else if(new File(filePath).isFile()){	//��������ļ�
			Intent intent = getIntent();	//�½�Intent������ת
			
			Bundle bundle = intent.getExtras();
			int from = bundle.getInt("FileOperation");
			Log.i("FileExplorer", bundle.getInt("FileOperation") + "");
			intent.putExtra("file_path", filePath);	//���ļ�·������
			intent.putExtra("name", name);			//���ļ�������
			Log.i("FileExplorer�ļ�·��", filePath);
			Log.i("FileExplorer�ļ���", name);
			
			setResult(from, intent);			//���ý����
			finish();							//finish()������ת��startActivityForResult()������
		}
	}

	
	/**
	 * �����ļ�����������Ŀ�������
	 */
	
	/*@Override
	protected void onListItemClick(ListView listview, View view, int position, long id) {
		Log.d("ListViewClicked", (String) mData.get(position).get("file_path"));	//��ʾ��������ļ���·��
		if ((Integer) mData.get(position).get("icon") == R.drawable.folder) {	//�����������ļ�
			filePath = (String) mData.get(position).get("file_path");
			mData = getList(filePath);
			MyAdapter adapter = new MyAdapter(this);
			setListAdapter(adapter);
		} else {		//�������Ŀ¼
			finishWithResult((String) mData.get(position).get("file_path"));
		}
		
//		title = (TextView) view.findViewById(R.id.title);
//		info = (TextView) view.findViewById(R.id.info);		//��ȡ�����Ŀ���������������ã�����������ȡ�����Ҫ��Ȩ�޺��ṩж��ѡ��
//		System.out.println("title: " + title.getText().toString() + "info: " + info.getText().toString());
	}
*/
	

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return fileList.size();
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		//������ListView���������
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			File file = (File) getItem(position);
          if (convertView == null) {
			
//			if(position == 1 && !file.getPath().equals(sdCardFilePath)) {
            	holder = new ViewHolder();
 				convertView = mInflater.inflate(R.layout.file_info_row, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.file_path = (TextView) convertView.findViewById(R.id.file_path);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.icon.setBackgroundResource((Integer) fileList.get(position).get("icon"));
			holder.name.setText((String) fileList.get(position).get("name"));
			holder.file_path.setText((String) fileList.get(position).get("file_path"));
			return convertView;
		}
	}
}