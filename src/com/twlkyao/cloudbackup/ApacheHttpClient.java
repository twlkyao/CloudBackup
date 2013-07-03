/**
 * ���ߣ�		����
 * �޸��ߣ�	��ʿ��
 * ���ڣ�		2013.4.8
 * ���ܣ�		��ͬ���ķ�ʽ����HTTP���󣬰���POST��GET����
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
 * ��ͬ����ʽ����Http����
 */
public class ApacheHttpClient {
	/**
	 * ��Get��ʽ��������
	 * @param url ����·��
	 * @return response Http�����״̬
	 */
	public String httpGet(String url) {
		String response = null;				//���ڼ�¼���ؽ��
		HttpClient httpclient = new DefaultHttpClient();	//�½�һ��DefaultHttpClientʵ��������ִ��Http����
		HttpGet httpGet = new HttpGet(url);	//�½�HttpGetʵ��
		HttpResponse httpResponse;			//�½�һ��HttpResponseʵ�������ڵõ�Http������Ӧ
		try {
			//ʹ��execute��������HTTP GET���󣬲�����HttpResponse����
			httpResponse = httpclient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();	//���״̬��
			if(statusCode==HttpStatus.SC_OK) {		//״̬��ΪSC_OK
				//��÷��ؽ��
				System.out.println("״̬��:" + statusCode);
				response = EntityUtils.toString(httpResponse.getEntity());
			} else {								//����
				response = "�����룺"+statusCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;							//���ؽ��
	}

	/**
	 * ��Post��ʽ��������
	 * @param url �����ַ
	 * @param params ���� ��Post��ʽ������NameValuePair[]���д������
	 * @return
	 * @throws Exception
	 */
	public String httpPost(String url, List<NameValuePair> params) throws Exception
	{
		String response = null;
		HttpClient httpclient = new DefaultHttpClient();	//�½�һ��DefaultHttpClientʵ��������ִ��Http����
		//����HttpPost����
		HttpPost httppost = new HttpPost(url);				//�½�HttpPostʵ��
		try {
			//����httpPost�������
			httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));	//����Url����
			//ʹ��execute��������HTTP Post���󣬲�����HttpResponse����
			HttpResponse httpResponse = httpclient.execute(httppost);		//ִ��HttpPost����
			int statusCode = httpResponse.getStatusLine().getStatusCode();	//�õ�HttpPost�����״̬��
			if(statusCode==HttpStatus.SC_OK)	//״̬��ΪSC_OK
			{
				//��÷��ؽ��
				response = EntityUtils.toString(httpResponse.getEntity());	//�����Ϣʵ��
			}
			else								//������÷��ص�״̬��
			{
				response = "�����룺"+statusCode;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return response;						//����HttpPost����Ľ��
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