package com.noah.npardon.grind.net;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class WSConnexionHTTPS extends AsyncTask<String,Integer,String> {
    private final String base_url = "http://172.20.63.43:80/grindAndroid/index.php?";
    private final String tmdb = "https://api.themoviedb.org/";
    private final String tmbdToken = Constants.TMDB_API_KEY;
    public static final MediaType JSON = MediaType.get("text/plain; charset=utf-8");
    private static OkHttpClient client = null;
    Request request;

    public WSConnexionHTTPS() {
        if( client==null) {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
                newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
                newBuilder.hostnameVerifier((hostname, session) -> true);
                newBuilder.cookieJar(new CookieJar() {
                    List<Cookie> cookies;

                    @Override
                    public void saveFromResponse(HttpUrl hu, List<Cookie> list) {
                        this.cookies = list;
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl hu) {
                        if (cookies != null) {
                            return cookies;
                        } else {
                            return new ArrayList<>();
                        }
                    }
                });
                client = newBuilder.build();
            }catch (Exception ex){
                Log.e("WSHTTPS",ex.getMessage()) ;
            }
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        if (strings.length == 2 && strings[1].equals("TMDB")){
             request = new Request.Builder()
                    .url(tmdb + strings[0])
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer "+tmbdToken)
                    .build();
            Log.d("strLen 2", "doInBackground: "+tmdb+strings[0]);
        }
        else if (strings.length==2){
            request = new Request.Builder()
                    .addHeader("Authorization", "Bearer "+strings[1])
                    .url(base_url + strings[0])
                    .build();
            Log.d("login req", "doInBackground: "+strings[0]);
        }else{
            request = new Request.Builder()
                    .url(base_url + strings[0])
                    .build();
            Log.d("basic req", "doInBackground: "+strings[0]);
        }
        try ( Response response = client.newCall(request).execute()) {
            String resp = response.body().string();
            Log.d("RESPONSE",resp) ;
            return resp ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }
}