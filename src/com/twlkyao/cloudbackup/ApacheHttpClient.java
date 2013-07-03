/**
 * 作者：		网络
 * 修改者：	齐士垚
 * 日期：		2013.4.8
 * 功能：		用同步的方式发送HTTP请求，包括POST和GET方法
 */
package com.twlkyao.cloudbackup;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


/**
 *@author coolszy
 *@date 2011-5-29
 *@blog http://blog.csdn.net/coolszy
 */

/**
 * 以同步方式发送Http请求
 */
public class ApacheHttpClient {
	/**
	 * 以Get方式发送请求
	 * @param url 请求路径
	 * @return response Http请求的状态
	 */
	public String httpGet(String url) {
		String response = null;				//用于记录返回结果
		HttpClient httpclient = new DefaultHttpClient();	//新建一个DefaultHttpClient实例，用于执行Http请求
		HttpGet httpGet = new HttpGet(url);	//新建HttpGet实例
		HttpResponse httpResponse;			//新建一个HttpResponse实例，用于得到Http请求响应
		try {
			//使用execute方法发送HTTP GET请求，并返回HttpResponse对象
			httpResponse = httpclient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();	//获得状态码
			if(statusCode==HttpStatus.SC_OK) {		//状态码为SC_OK
				//获得返回结果
				System.out.println("状态码:" + statusCode);
				response = EntityUtils.toString(httpResponse.getEntity());
			} else {								//出错
				response = "返回码："+statusCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;							//返回结果
	}

	/**
	 * 以Post方式发送请求
	 * @param url 请求地址
	 * @param params 参数 ，Post方式必须用NameValuePair[]阵列储存参数
	 * @return
	 * @throws Exception
	 */
	public String httpPost(String url, List<NameValuePair> params) throws Exception
	{
		String response = null;
		HttpClient httpclient = new DefaultHttpClient();	//新建一个DefaultHttpClient实例，用于执行Http请求
		//创建HttpPost对象
		HttpPost httppost = new HttpPost(url);				//新建HttpPost实例
		try {
			//设置httpPost请求参数
			httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));	//设置Url编码
			//使用execute方法发送HTTP Post请求，并返回HttpResponse对象
			HttpResponse httpResponse = httpclient.execute(httppost);		//执行HttpPost请求
			int statusCode = httpResponse.getStatusLine().getStatusCode();	//得到HttpPost请求的状态码
			if(statusCode==HttpStatus.SC_OK)	//状态码为SC_OK
			{
				//获得返回结果
				response = EntityUtils.toString(httpResponse.getEntity());	//获得消息实体
			}
			else								//出错，获得返回的状态码
			{
				response = "返回码："+statusCode;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return response;						//返回HttpPost请求的结果
	}
	
	/*public DefaultHttpClient getClient() {
        DefaultHttpClient ret = null;

        // sets up parameters
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");
        params.setBooleanParameter("http.protocol.expect-continue", false);
        
        // registers schemes for both http and https
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
        ret = new DefaultHttpClient(manager, params);
        return ret;
    }
	*/
	public class TrivialTrustManager implements X509TrustManager {
	    public void checkClientTrusted(X509Certificate[] chain, String authType) throws 
	CertificateException {}
	    public void checkServerTrusted(X509Certificate[] chain, String authType) throws 
	CertificateException {}
	    public X509Certificate[] getAcceptedIssuers() {
	        return new X509Certificate[0];
	    }
	}
}