package com.twlkyao.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class _FakeX509TrustManager implements X509TrustManager {

    private static TrustManager[] trustManagers;
    
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {
    	
    };

    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    
    }


    public boolean isClientTrusted(X509Certificate[] chain) {
    	return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
    	return true;
    }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return _AcceptedIssuers;
    }

    public static void allowAllSSL() {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

					@Override
					public boolean verify(String hostname, SSLSession session) {
						// TODO Auto-generated method stub
						return true;
					}

            });

            SSLContext context = null;
            if (trustManagers == null) {
                    trustManagers = new TrustManager[] { new _FakeX509TrustManager() };
            }

            try {
                    context = SSLContext.getInstance("TLS");
                    context.init(null, trustManagers, new SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
            } catch (KeyManagementException e) {
                    e.printStackTrace();
            }

            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }


	@Override
	public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
			String authType) throws java.security.cert.CertificateException {
		// TODO Auto-generated method stub
		
	}

}