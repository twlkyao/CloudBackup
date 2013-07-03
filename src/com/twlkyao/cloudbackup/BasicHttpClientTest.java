package com.twlkyao.cloudbackup;

import android.test.AndroidTestCase;
import android.util.Log;

/**
 * @author  coolszy
 * @blog    http://blog.csdn.net/coolszy
 */
public class BasicHttpClientTest extends AndroidTestCase
{
	private static final String TAG = "HttpClientTest";
	//10.0.2.2
	String urlStr = "https://pcs.baidu.com/rest/2.0/pcs/quota";
	String params = "method=info&access_token=3.811a254908d094012df764a38882a179.2592000.1348661720.2233553628-238347";
	public void testHttpGet()
	{
		BasicHttpClient client = new BasicHttpClient();
		String response = client.httpGet(urlStr+"?"+params);
		Log.i(TAG, response);
	}

	public void testHttpPost()
	{
		BasicHttpClient client = new BasicHttpClient();
		String response = client.httpPost(urlStr, params);
		Log.i(TAG, response);
	}
}