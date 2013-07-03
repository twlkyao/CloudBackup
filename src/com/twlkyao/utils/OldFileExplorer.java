package com.twlkyao.utils;

/**
 * 作者：		网络
 * 修改者：	齐士垚
 * 日期：		2013.5.10
 * 功能：		文件管理器
 * 待优化：	现在“返回上层目录”和“返回父目录”一直存在
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
	private List<Map<String, Object>> fileList;	//用于存储文件信息
	private SimpleAdapter simpleAdapter;	//用于list和listview之间的适配
	private ListView listview = null;		//ListView对象
	private static int i = 1;
	private String sdCardFilePath = Environment.getExternalStorageDirectory().toString();	//根目录
	public static int upload = 1;		//上传文件请求码
	public static int download = 2;		//下载文件请求码
	public static int make_dir = 3;		//新建目录请求码
	String resultCode;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*findViews();
		refreshListView();
		setListeners();*/
		
		fileList = getList(sdCardFilePath);				//获得文件的列表
		MyAdapter myAdapter = new MyAdapter(this);		//实例化一个新的适配器
		setListAdapter(myAdapter);						//设置适配器
		
		Log.i("生命周期", "onCreate");
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
		
		fileList = getList(sdCardFilePath);		//调用自定义函数，获得安装程序的列表
		simpleAdapter = new SimpleAdapter(this, fileList, R.layout.file_info_row,
				new String[] { "icon", "name", "file_path" }, new int[] { R.id.icon, R.id.name, R.id.file_path });		//新建一个Adapter
		listview.setAdapter(simpleAdapter);		//设置Adapter
	}
	*/
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i("生命周期", "onPause");
		super.onPause();
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.i("生命周期", "onRestart");
		super.onRestart();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i("生命周期", "onResume");
		super.onResume();
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i("生命周期", "onStart");
		super.onStart();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i("生命周期", "onStop");
		super.onStop();
	}


	/**
	 * @function 返回filePath目录下的文件信息
	 * @param filePath 文件路径名
	 * @return 文件信息列表
	 */
	private List<Map<String, Object>> getList(String currentfilePath) {	
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();	//新建一个List用于获取最后的文件列表
		Map<String, Object> map = null;			//声明一个Map实例，用于存储数据
		File file = new File(currentfilePath);			//利用filePath目录构建一个新的文件
		File[] files = file.listFiles();		//返回当前文件目录下的文件列表
		
		//返回上一层目录
//		if (!file.equals(Environment.getExternalStorageDirectory())) {		//如果没有达到SD卡根目录
		if (file.getPath() != sdCardFilePath) {
			//添加SD卡根目录
			map = new HashMap<String,Object>();		//实例化map
			map.put("icon", R.drawable.root);	//设置根目录图标
			map.put("name", "返回SD卡根目录");			//设置返回根目录名
//			map.put("file_path", Environment.getExternalStorageDirectory().toString());			//设置返回根目录路径
			map.put("file_path", sdCardFilePath);
			list.add(map);						//将数据添加到list中
			
			Log.i("SD根目录", file.getPath());
			/*Log.i(i + "目录", filePath);
			i ++;*/
			
			map = new HashMap<String, Object>();//实例化map
			map.put("icon", R.drawable.uplevel);//设置图标
			map.put("name", "返回上层目录");		//设置标题
			map.put("file_path", file.getParent());	//设置详细信息，获得当前文件的父目录的路径
			list.add(map);						//将数据添加到list中
			
			Log.i("返回 当前目录", file.getPath());
			Log.i("返回 父目录", file.getParent());
			Log.i("返回 当前Sdcard目录", sdCardFilePath);
			
		} 
		
//		if(file.getParent() != sdCardFilePath && file.getPath() != sdCardFilePath && file.getPath() != "/" && file.getParent() != "/mnt/") {			//如果未到达指定目录，则添加返回上层目录
		/*	
		if(file.getPath() != sdCardFilePath && file.getParent() != sdCardFilePath) {//添加返回上层目录
			map = new HashMap<String, Object>();//实例化map
			map.put("icon", R.drawable.file_up);//设置图标
			map.put("name", "返回上层目录");		//设置标题
			map.put("file_path", file.getParent());	//设置详细信息，获得当前文件的父目录的路径
			list.add(map);						//将数据添加到list中
			
			Log.i("返回 当前目录", file.getPath());
			Log.i("返回 父目录", file.getParent());
			Log.i("返回 当前Sdcard目录", sdCardFilePath);
			
//			Log.i(i + "目录", filePath);
//			i ++;
		}*/
		
		//一般的目录或者文件
		if(files != null) {	//非根目录，而且是目录文件
			for (int i = 0; i < files.length; i++) {	//遍历目录下所有的文件和目录
				map = new HashMap<String, Object>();	//实例化map
				if (files[i].isDirectory())				//如果是目录，设置目录图标
					map.put("icon", R.drawable.folder);
				else if(files[i].isFile())				//如果是文件，设置文件图标
					map.put("icon", R.drawable.file);
				map.put("name", files[i].getName());	//获得文件或者目录名
				map.put("file_path", files[i].getPath());	//得到文件或者目录的路径
				
				list.add(map);							//在ListView中添加数据
				
//				Log.i("file.getPath()", file.getPath());
				
//				Log.i(i + "目录", filePath);
//				i ++;
			}
		}
		if(null == files) {	//空目录
			map = new HashMap<String, Object>();
			map.put("icon", R.drawable.empty);
			map.put("name", "空目录");
			map.put("file_path", "");
			list.add(map);
			
//			Log.i("file.getPath()", file.getPath());
			
			/*Log.i(i + "目录", filePath);
			i ++;*/
			
		}
		setTitle(currentfilePath);		//设置标题
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
		if(newFile.isDirectory()){	//如果点击的是文件目录，则打开文件目录
				
			Log.i("name", name);
			fileList = getList(newFile.getPath());		//获得文件的列表
			MyAdapter myAdapter = new MyAdapter(this);	//实例化一个新的适配器
			setListAdapter(myAdapter);		//设置适配器
		} 
		else if(new File(filePath).isFile()){	//点击的是文件
			Intent intent = getIntent();	//新建Intent用于跳转
			
			Bundle bundle = intent.getExtras();
			int from = bundle.getInt("FileOperation");
			Log.i("FileExplorer", bundle.getInt("FileOperation") + "");
			intent.putExtra("file_path", filePath);	//将文件路径传入
			intent.putExtra("name", name);			//将文件名传入
			Log.i("FileExplorer文件路径", filePath);
			Log.i("FileExplorer文件名", name);
			
			setResult(from, intent);			//设置结果码
			finish();							//finish()用于跳转回startActivityForResult()调用者
		}
	}

	
	/**
	 * 设置文件管理器的条目点击动作
	 */
	
	/*@Override
	protected void onListItemClick(ListView listview, View view, int position, long id) {
		Log.d("ListViewClicked", (String) mData.get(position).get("file_path"));	//显示被点击的文件的路径
		if ((Integer) mData.get(position).get("icon") == R.drawable.folder) {	//如果点击的是文件
			filePath = (String) mData.get(position).get("file_path");
			mData = getList(filePath);
			MyAdapter adapter = new MyAdapter(this);
			setListAdapter(adapter);
		} else {		//点击的是目录
			finishWithResult((String) mData.get(position).get("file_path"));
		}
		
//		title = (TextView) view.findViewById(R.id.title);
//		info = (TextView) view.findViewById(R.id.info);		//获取点击项目的完整包名，备用，可以用来获取软件需要的权限和提供卸载选项
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

		//用于向ListView中添加数据
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