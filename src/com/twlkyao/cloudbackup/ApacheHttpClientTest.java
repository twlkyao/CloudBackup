package com.twlkyao.cloudbackup;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.test.AndroidTestCase;
import android.util.Log;

/**
 * @author  coolszy
 * @blog    http://blog.csdn.net/coolszy
 */
public class ApacheHttpClientTest extends AndroidTestCase
{
	private static final String TAG = "ApacheHttpClientTest";
	
	String response = "";
	public void testHttpGet()
	{
		String url = "http://dts.xa.ftn.qq.com/ftn_handler/?ver=12345&ukey=4bb9288183515aa6d2d2b101c7b9230e3d33e2df144740372a349768bc9538b386b24f84b6e538ee";
		ApacheHttpClient client = new ApacheHttpClient();
		try
		{
			response = client.httpGet(url);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		Log.i(TAG,response);
		
		Log.i("HttpGet", "³É¹¦");
//		System.out.println(response);
	}

	public void testHttpPost()
	{
		String url = " https://graph.qq.com/weiyun/upload_file";
		ApacheHttpClient client = new ApacheHttpClient();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sid","4JCqIFerLy17Ei18"));
		params.add(new BasicNameValuePair("r","1e1ac98ea006b7625f0747e0d5b92f66"));
		try
		{
			response = client.httpPost(url, params);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		Log.i(TAG, "####"+response);
	}
}
