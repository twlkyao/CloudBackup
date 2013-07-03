/**
 * 作者：		齐士垚
 * 日期：		2013.3.10
 * 功能：		测试百度云的存储功能
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
		APP ID：100405970
		KEY：9ea6a71997ab753a189bbe36692cb752
	 * */
//	TextView textview;		//用于显示返回结果
//	TextView baseTextview;
	Button buttonLogin;			//用于获得QQ登陆授权
	Button buttonLogout;		//用于取消QQ登陆授权
	String url = "https://openmobile.qq.com/oauth2.0/m_authorize";
							//用于记录Http Get请求地址
	String url1 = "https://pcs.baidu.com/rest/2.0/pcs/file?method=download&access_token=3.d9000194f4b5d2da3fe8b6f850ace082.2592000.1348645419.2233553628-248414&path=%2Fapps%2F%E6%B5%8B%E8%AF%95%E5%BA%94%E7%94%A8%2F%2F01.jpg";
	String result = "";
	String authorcode = "https://graph.z.qq.com/moc2/authorize";	//用于存储QQ授权基础地址
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
        
     // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
     // 其中APP_ID是分配给第三方应用的appid，类型为String。
//     mTencent = Tencent.createInstance(appid, this.getApplicationContext());
//     // 1.4_edited:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
//     // 初始化视图
//     findViews();
//     setListeners();

    }
   
    //应用调用Andriod_SDK_V1.4接口时，如果要成功接收到回调，需要在调用接口的Activity的onActivityResult方法中增加如下代码：
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          mTencent.onActivityResult(requestCode, resultCode, data) ;
          Log.i("CloudBackup","onActivity");
    }
    
    //实现相应监听器
    public void setListeners() {
    	
    	
    	//实现登陆QQ按钮监听器
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
    	
    	//实现注销QQ按钮监听器
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
    //登录时的回调函数
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
    
    //IUiListener：调用SDK已经封装好的接口时，例如：登录、快速支付登录、应用分享、应用邀请等接口。
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
    //IRequestListener：使用requestAsync、request等通用方法调用sdk未封装的接口时，例如上传图片、查看相册等。
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
    		//1.4版本中IRequestListener 新增两个异常
    	
    	@Override
    	public void onNetworkUnavailableException(NetworkUnavailableException e, Object state){
    		// 当前网络不可用时触发此异常
    	}
    	
    	@Override
    	public void onHttpStatusException(HttpStatusException e, Object state) {
    		// http请求返回码非200时触发此异常
    	}
    	
    	public void onUnknowException(Exception e, Object state) {
    		// 出现未知错误时会触发此异常
    	}
    }
}