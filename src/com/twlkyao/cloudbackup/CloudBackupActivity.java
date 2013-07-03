/**
 * @function 利用微云开放平台备份数据。
 * @author 齐士垚
 * @time 201.3.5.15
 */
package com.twlkyao.cloudbackup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.tauth.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class CloudBackupActivity extends Activity implements OnClickListener {

    public static final String appid = "100405970";		//appid相当于client_id 
//    public static final String appid = "100698797";	//http://op.open.qq.com/index.php?mod=appinfo&act=main&appid=100698797
    Intent intent = new Intent();	//新建一个Intent实例用于Activity跳转
//    String getaccess_token = "19856441A8EFEE4AACE36093D09DC227";
//    String openid = "2C3C2A5BE69AA214E81E86023435D882";
//    String pay_token = "43FEEBD7DB63B09E723F7E1DD9D32FA9";
//    String access_token = "9AB0B13BB0F2FE14385EB536EF314884";
    
//    private static final String SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,get_app_friends,"
//            + "add_share,add_topic,list_album,upload_pic,add_album,set_user_face,get_vip_info,get_vip_rich_info,get_intimate_friends_weibo,match_nick_tips_weibo";

    /**
     * appid == oauth_consumer_key == client_id
     * appkey == oauth_consumer_secret == client_secret
     * 应用内回调地址：redirect_uri = auth://tauth.qq.com/
     */
    
    private static final String SCOPE = "all";
    private static final int REQUEST_UPLOAD_PIC = 1000;
	private static final int REQUEST_SET_AVATAR = 2;
    private ImageView mLoginButton;		//登陆按钮
    private Tencent mTencent;			//腾讯认证实例
//    private TextView mBaseMessageText;	//用于显示信息类型
//    private TextView mMessageText;		//用于显示详细信息
    private Handler mHandler;
    private Dialog mProgressDialog;
    public static String access_token;			//用于记录access_token
    public static String openid;				//用于记录openid
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 固定竖屏
        setContentView(R.layout.main);
      
        final Context ctxContext = this.getApplicationContext();	//返回应用的上下文
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中appid是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(appid, ctxContext);	//在当前Activity创建一个Tencent实例，用于认证
        mHandler = new Handler();
        mProgressDialog = new ProgressDialog(CloudBackupActivity.this);
        initViews();
        /**
         * 不需要调试即可跳转
         */
        intent.setClass(CloudBackupActivity.this, FileExplorer.class);
        startActivityForResult(intent, 0);
        /**
         * 
         */
    }
    
    //用于父子Activity进行通信和状态返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	//请求码，返回码，传递的数据
        // must call mTencent.onActivityResult.
        if (!mTencent.onActivityResult(requestCode, resultCode, data)) {
            if (data != null) {
                if (requestCode == REQUEST_UPLOAD_PIC) {
//                    doUploadPic(data.getData());
                } else if(requestCode == REQUEST_SET_AVATAR){
					doSetAvatar(data.getData());
				}
            }
        }
    }
    private void initViews() {	//找到视图，并且设置监听器
//        mBaseMessageText = (TextView) findViewById(R.id.baseTextview);
//        mMessageText = (TextView) findViewById(R.id.textview);
    	mLoginButton = (ImageView) this.findViewById(R.id.login_btn);
        updateLoginButton();	//更新登陆按钮的状态
        mLoginButton.setOnClickListener(this);	//设置按钮监听器
      
	}

    //更新登陆按钮的状态
    private void updateLoginButton() {	//根据会话是否有效，设置登陆按钮的状态
        mLoginButton
                .setImageResource(mTencent.isSessionValid() ? R.drawable.com_tencent_open_logout
                        : R.drawable.com_tencent_open_login);
    }

    //根据用户点击的不同项目，进行处理
    @Override
    public void onClick(View v) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        switch (v.getId()) {
            case R.id.login_btn:		//登陆
                onClickLogin();
                showResult(null, null);			//显示appid,oauthorth_token
                v.startAnimation(shake);
                break;
            default:
                break;
        }
    }

    //点击登陆按钮操作
    private void onClickLogin() {
        if (!mTencent.isSessionValid()) {	//会话无效，新建登陆实例
        	 IUiListener listener = new BaseUiListener() {
                 @Override
                 protected void doComplete(JSONObject values) {
                	 try {
                		access_token = values.get("access_token").toString();
                		openid = values.get("openid").toString();
                		Log.i("access_token", values.get("access_token").toString());
                		Log.i("openid", values.get("openid").toString());
                		Log.i("pay_token", values.get("pay_token").toString());
                		System.out.println("values:" + values);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                     updateLoginButton();
                 }
             };
            mTencent.login(this, SCOPE, listener);
        } else {	//会话有效，登陆
        	mTencent.logout(this);
            updateLoginButton();
        }
    }
    
   

    //判断是否有效
    private boolean ready() {
        boolean ready = mTencent.isSessionValid()	//会话有效
                && mTencent.getOpenId() != null;	//openid不为空
        if (!ready)		//会话无效或者openid为空
            Toast.makeText(this, "login and get openId first, please!",
                    Toast.LENGTH_SHORT).show();
        return ready;
    }

   

    //点击获取openid按钮
    private void onClickOpenId() {
        if (mTencent.isSessionValid()) {	//如果会话有效

//            mTencent.requestAsync(Constants.GRAPH_OPEN_ID, null,
//                    Constants.HTTP_GET, new BaseApiListener(), null);

            mProgressDialog.show();
        }
    }

    /**
    //上传图片
    private void doUploadPic(Uri uri) {
        if (ready()) {
            Bundle params = new Bundle();

            byte[] buff = null;
            try {
                InputStream is = getContentResolver().openInputStream(uri);
                ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outSteam.write(buffer, 0, len);
                }
                outSteam.close();
                is.close();
                buff = outSteam.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            params.putByteArray("picture", buff);// 必须.上传照片的文件名以及图片的内容（在发送请求时，图片内容以二进制数据流的形式发送，见下面的请求示例），注意照片名称不能超过30个字符。
            params.putString("photodesc", "QQ登陆SDK：UploadPic测试" + new Date());// 照片描述，注意照片描述不能超过200个字符。
            params.putString("title",
                    "QQ登陆SDK：UploadPic测试" + System.currentTimeMillis() + ".png");// 照片的命名，必须以.jpg,
                                                                                 // .gif,
                                                                                 // .png,
                                                                                 // .jpeg,
                                                                                 // .bmp此类后缀结尾。
            // bundle.putString("albumid",
            // "564546-asdfs-feawfe5545-45454");//相册id，不填则传到默认相册
            params.putString("x", "0-360");// 照片拍摄时的地理位置的经度。请使用原始数据（纯经纬度，0-360）。
            params.putString("y", "0-360");// 照片拍摄时的地理位置的纬度。请使用原始数据（纯经纬度，0-360）。

//            mTencent.requestAsync(Constants.GRAPH_UPLOAD_PIC, params,
//                    Constants.HTTP_POST, new BaseApiListener(), null);

            mProgressDialog.show();
        }
    }
    */
  //设置头像
  	private void doSetAvatar(Uri uri) {
  		Bundle params = new Bundle();
  		params.putString(Constants.PARAM_AVATAR_URI, uri.toString());
  		//这个return_activity是可选的
  		//params.putString(Constants.PARAM_AVATAR_RETURN_ACTIVITY, "com.tencent.sample.ReturnActivity");
  		mTencent.setAvatar(this, params);
  	}
    
  	/**
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
            showResult("IRequestListener.onConnectTimeoutException:", arg0.getMessage());

        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException arg0,
                Object arg1) {
            showResult("IRequestListener.SocketTimeoutException:", arg0.getMessage());
        }

        @Override
        public void onUnknowException(Exception arg0, Object arg1) {
            showResult("IRequestListener.onUnknowException:", arg0.getMessage());
        }


        @Override
        public void onHttpStatusException(HttpStatusException arg0, Object arg1) {
            showResult("IRequestListener.HttpStatusException:", arg0.getMessage());
        }


        @Override
        public void onNetworkUnavailableException(NetworkUnavailableException arg0, Object arg1) {
            showResult("IRequestListener.onNetworkUnavailableException:", arg0.getMessage());
        }
    }
    */
    private void showResult(final String base, final String msg) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
//                mBaseMessageText.setText(base);
//                mMessageText.setText(msg);
            }
        });
    }

    
    private class BaseUiListener implements IUiListener {	//调用SDK已经封装好的接口

        @Override
        public void onComplete(JSONObject response) {
//            mBaseMessageText.setText("onComplete:");
//            mMessageText.setText(response.toString());
            doComplete(response);
            Log.i("onComplete","登陆成功");
            intent.setClass(CloudBackupActivity.this, FileOperation.class);
            startActivityForResult(intent, 0);
        }

        protected void doComplete(JSONObject values) {
        }

        @Override
        public void onError(UiError e) {
            showResult("onError:", "code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
            Log.i("onError","登陆失败");
        }

        @Override
        public void onCancel() {
            showResult("onCancel", "");
            Log.i("onCancel","登陆取消");
        }
    }
}
