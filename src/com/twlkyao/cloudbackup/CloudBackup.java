/**
 * ���ߣ�		��ʿ��
 * ���ڣ�		2013.3.10
 * ���ܣ�		���԰ٶ��ƵĴ洢����
 *Client_id:yfc7NogyTB1M0CU8lMoEmKd4
 *AccessToken:3.628cb1aefa96a5ac946d69daf47d258e.2592000.1366774641.2466441286-638735
 *


 * http://openapi.baidu.com/oauth/2.0/login_success#expires_in=2592000
 * &access_token=3.628cb1aefa96a5ac946d69daf47d258e.2592000.1366774641.2466441286-638735
 * &session_secret=e9e238fd414e7129423b0025522bdf33
 * &session_key=94rlZZIWryVaw%2BKTHgdVM6m2iMzTV5OZ14760Nr5HZkfOLkS5dLVj0yG4oYRrIEk5nvXRfsCuqo0goTWb5qkAeoRDgUqvp0%3D
 * &scope=basic+netdisk
 */
package com.twlkyao.cloudbackup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.widget.Button;
import android.util.Log;
import android.view.View;

public class CloudBackup extends Activity {

	/*
	 * 	CloudBackup
		APP ID��100405970
		KEY��9ea6a71997ab753a189bbe36692cb752
	 * */
//	TextView textview;		//������ʾ���ؽ��
//	TextView baseTextview;
	Button buttonLogin;			//���ڻ��QQ��½��Ȩ
	Button buttonLogout;		//����ȡ��QQ��½��Ȩ
	String url = "https://openmobile.qq.com/oauth2.0/m_authorize";
							//���ڼ�¼Http Get�����ַ
	String url1 = "https://pcs.baidu.com/rest/2.0/pcs/file?method=download&access_token=3.d9000194f4b5d2da3fe8b6f850ace082.2592000.1348645419.2233553628-248414&path=%2Fapps%2F%E6%B5%8B%E8%AF%95%E5%BA%94%E7%94%A8%2F%2F01.jpg";
	String result = "";
	String authorcode = "https://graph.z.qq.com/moc2/authorize";	//���ڴ洢QQ��Ȩ������ַ
	String response_type = "token";
	String appid = "100405970";
	String openid = "2C3C2A5BE69AA214E81E86023435D882";
	String access_token = "D4959AA8310C52DC156EB59221F6C53E";
	String redirect_uri = "http://alientech.sinaapp.com";
	String state = "test";
	private Handler mHandler;
	private Dialog mProgressDialog;
	Tencent mTencent;
	private static final String SCOPE = "all";
	/*
	 * https://graph.qq.com/oauth2.0/authorize?
	 * response_type=code&
	 * client_id=[YOUR_APPID]&
	 * redirect_uri=[YOUR_REDIRECT_URI]&
	 * state= [THE_STATE]
	 * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        result = new ApacheHttpClient().httpGet(url + "?" + "response_type" + response_type + "&client_id" + appid + "&redirect_uri" + redirect_uri);
//        textview.setText(result);
        Log.i("CloudBackup","onCreate");	
        System.out.println(result);
        
     // Tencent����SDK����Ҫʵ���࣬�����߿�ͨ��Tencent�������Ѷ���ŵ�OpenAPI��
     // ����APP_ID�Ƿ����������Ӧ�õ�appid������ΪString��
//     mTencent = Tencent.createInstance(appid, this.getApplicationContext());
//     // 1.4_edited:�˴�����������������Ӧ�ó����ȫ��context����ͨ��activity��getApplicationContext������ȡ
//     // ��ʼ����ͼ
//     findViews();
//     setListeners();

    }
   
    //Ӧ�õ���Andriod_SDK_V1.4�ӿ�ʱ�����Ҫ�ɹ����յ��ص�����Ҫ�ڵ��ýӿڵ�Activity��onActivityResult�������������´��룺
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          mTencent.onActivityResult(requestCode, resultCode, data) ;
          Log.i("CloudBackup","onActivity");
    }
    
    //ʵ����Ӧ������
    public void setListeners() {
    	
    	
    	//ʵ�ֵ�½QQ��ť������
    	buttonLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				String url = authorcode + "?" + "response_type=" + response_type + 
//						"&client_id=" + appid + "&redirect_uri=" + redirect_uri + 
//						"&state=" + state;
				result = new ApacheHttpClient().httpGet(url);
//				textview.setText(result);
//				Log.i("Click", result);
				doLogin();
			}
		});
    	
    	//ʵ��ע��QQ��ť������
    	buttonLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mTencent.logout(getApplicationContext());
			}
		});
    }
    
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.qqlogin:
//                onClickLogin();
//                break;
//            case R.id.qqlogout:
//                onClickLogout();
//                break;
            default:
                break;
        }
    }
    //��¼ʱ�Ļص�����
    private void doLogin() {
    	IUiListener listener = new BaseUiListener() {
    		@Override
    		protected void doComplete(JSONObject values) {
    		}
    	};
    	Log.i("CloudBackup","doLogin");
    	mTencent.login(this, SCOPE, listener);
    }

  
    private void onClickLogin() {
        if (!mTencent.isSessionValid()) {
            IUiListener listener = new BaseUiListener() {
                @Override
                protected void doComplete(JSONObject values) {
//                    updateLoginButton();
                }
            };
            mTencent.login(this, SCOPE, listener);
        } else {
            mTencent.logout(this);
//            updateLoginButton();
        }
    }

    private void onClickLogout() {
    	Log.i("CloudBackup","onClickLogout");
    	mTencent.logout(this);
    }
    private void showResult(final String base, final String msg) {
        
    	mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
//                baseTextview.setText(base);
//                textview.setText(msg);
            }
        });
    }
    
    //IUiListener������SDK�Ѿ���װ�õĽӿ�ʱ�����磺��¼������֧����¼��Ӧ�÷���Ӧ������Ƚӿڡ�
    private class BaseUiListener implements IUiListener {
    	@Override
    	public void onComplete(JSONObject response) {
   		showResult("IRequestListener.onComplete:", response.toString());
   		doComplete(response);
//   	baseTextview.setText("onComplete:");
//    	textview.setText(response.toString());
    	}
    	protected void doComplete(JSONObject response) {
        }
    	@Override
    	public void onError(UiError e) {
    	showResult("onError:", "code:" + e.errorCode + ", msg:"
    	+ e.errorMessage + ", detail:" + e.errorDetail);
    	}
    	@Override
    	public void onCancel() {
    	showResult("onCancel", "");
    	}
    	}
    //IRequestListener��ʹ��requestAsync��request��ͨ�÷�������sdkδ��װ�Ľӿ�ʱ�������ϴ�ͼƬ���鿴���ȡ�
    private class BaseApiListener implements IRequestListener {
    	
    	@Override
    	public void onComplete(final JSONObject response, Object state) {
	    	showResult("IRequestListener.onComplete:", response.toString());
	    	doComplete(response, state);
    	}
    	protected void doComplete(JSONObject response, Object state) {
    	}
    	
    	@Override
    	public void onIOException(final IOException e, Object state) {
    		showResult("IRequestListener.onIOException:", e.getMessage());
    	}
    	
    	@Override
    	public void onMalformedURLException(final MalformedURLException e,
    	Object state) {
    		showResult("IRequestListener.onMalformedURLException", e.toString());
    	}
    	
    	@Override
    	public void onJSONException(final JSONException e, Object state) {
    		showResult("IRequestListener.onJSONException:", e.getMessage());
    	}
    	
    	@Override
    	public void onConnectTimeoutException(ConnectTimeoutException arg0,
    	Object arg1) {
    		// TODO Auto-generated method stub
    	}
    	
    	@Override
    	public void onSocketTimeoutException(SocketTimeoutException arg0,
    	Object arg1) {
    		// TODO Auto-generated method stub
    	}
    		//1.4�汾��IRequestListener ���������쳣
    	
    	@Override
    	public void onNetworkUnavailableException(NetworkUnavailableException e, Object state){
    		// ��ǰ���粻����ʱ�������쳣
    	}
    	
    	@Override
    	public void onHttpStatusException(HttpStatusException e, Object state) {
    		// http���󷵻����200ʱ�������쳣
    	}
    	
    	public void onUnknowException(Exception e, Object state) {
    		// ����δ֪����ʱ�ᴥ�����쳣
    	}
    }
}