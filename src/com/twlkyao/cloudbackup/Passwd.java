package com.twlkyao.cloudbackup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Passwd extends Activity {
	
	private EditText editTextPasswd;	//输入密码
	private Button btn_positive;		//确认密码
	private Button btn_reset;			//重置密码
	private Button btn_auto;			//使用自动密码
	private String strPasswd;			//记录密码
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passwd);
		findViews();
		setListeners();
	}

	public void findViews() {
		editTextPasswd = (EditText) this.findViewById(R.id.editText_passwd);
		btn_positive = (Button) this.findViewById(R.id.btn_positive);
		btn_reset = (Button) this.findViewById(R.id.btn_reset);
		btn_auto = (Button) this.findViewById(R.id.btn_auto);
	}
	
	public void setListeners() {
		
		/**
		 * 功能：	确认按钮监听器，点击后，获得密码输入文本框内的密码
		 */
		btn_positive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("Passwd", "确定");
				strPasswd = editTextPasswd.getText().toString();
				Log.i("passwd", strPasswd);
				Intent intent = getIntent();	//新建Intent用于跳转
				Bundle bundle = intent.getExtras();
				int from = bundle.getInt("FileExplorer");
				Log.i("Passwd", bundle.getInt("FileExplorer") + "");
				intent.putExtra("passwd", strPasswd);	//将密码传入
				Log.i("加密密码", strPasswd);
				
				setResult(from, intent);			//设置结果码
				finish();							//finish()用于跳转回startActivityForResult()调用者
			}
		});
		
		/**
		 * 功能：	重置按钮监听器，点击后，置空密码输入文本框的内容
		 */
		btn_reset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("Passwd", "重置");
				editTextPasswd.setText("");
				
			}
		});
		
		/**
		 * 功能：	自动按钮监听器，点击后，使用从网络端获取的密码
		 */
		btn_auto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("Passwd", "自动");
//				requestPasswd(String filePath, String SHA1, String MD5, String address);	//参数：文件路径，SHA1值，MD5值，网络服务器地址
				
				Intent intent = getIntent();	//新建Intent用于跳转
				Bundle bundle = intent.getExtras();
				int from = bundle.getInt("FileExplorer");
				setResult(from, intent);			//设置结果码
				finish();							//finish()用于跳转回startActivityForResult()调用者
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.passwd, menu);
		return true;
	}

	
}
