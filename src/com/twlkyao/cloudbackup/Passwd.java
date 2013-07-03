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
	
	private EditText editTextPasswd;	//��������
	private Button btn_positive;		//ȷ������
	private Button btn_reset;			//��������
	private Button btn_auto;			//ʹ���Զ�����
	private String strPasswd;			//��¼����
	
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
		 * ���ܣ�	ȷ�ϰ�ť������������󣬻�����������ı����ڵ�����
		 */
		btn_positive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("Passwd", "ȷ��");
				strPasswd = editTextPasswd.getText().toString();
				Log.i("passwd", strPasswd);
				Intent intent = getIntent();	//�½�Intent������ת
				Bundle bundle = intent.getExtras();
				int from = bundle.getInt("FileExplorer");
				Log.i("Passwd", bundle.getInt("FileExplorer") + "");
				intent.putExtra("passwd", strPasswd);	//�����봫��
				Log.i("��������", strPasswd);
				
				setResult(from, intent);			//���ý����
				finish();							//finish()������ת��startActivityForResult()������
			}
		});
		
		/**
		 * ���ܣ�	���ð�ť��������������ÿ����������ı��������
		 */
		btn_reset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("Passwd", "����");
				editTextPasswd.setText("");
				
			}
		});
		
		/**
		 * ���ܣ�	�Զ���ť�������������ʹ�ô�����˻�ȡ������
		 */
		btn_auto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("Passwd", "�Զ�");
//				requestPasswd(String filePath, String SHA1, String MD5, String address);	//�������ļ�·����SHA1ֵ��MD5ֵ�������������ַ
				
				Intent intent = getIntent();	//�½�Intent������ת
				Bundle bundle = intent.getExtras();
				int from = bundle.getInt("FileExplorer");
				setResult(from, intent);			//���ý����
				finish();							//finish()������ת��startActivityForResult()������
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
