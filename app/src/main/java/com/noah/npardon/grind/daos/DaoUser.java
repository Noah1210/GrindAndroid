package com.noah.npardon.grind.daos;

import android.util.Log;

import com.noah.npardon.grind.beans.User;
import com.noah.npardon.grind.net.WSConnexionHTTPS;

import org.json.JSONException;
import org.json.JSONObject;

public class DaoUser {
    private static DaoUser instance = null;

    public static DaoUser getInstance() {
        if (instance == null) {
            instance = new DaoUser();
        }
        return instance;
    }


    public void getLogin(String login, String password, DelegateAsyncTask delegate) {
        String url = "uc=main&action=login&login=" + login + "&password=" + password;
        Log.d("TAG", "getLogin: " + url);
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    getLoginFinished(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void getLoginFinished(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        User us = null;
        if (jo.getBoolean("success")) {
            JSONObject response = jo.getJSONObject("response");
            String login = response.getString("username");
            String fName = response.getString("first_name");
            String email = response.getString("email");
            String token = response.getString("token");
            us = new User(login, fName, email);
            us.setToken(token);
        }
        delegate.whenWSIsTerminated(us);
    }

    public void getLoginWithToken(String token, DelegateAsyncTask delegate) {
        String url = "uc=main&action=login";
        Log.d("TAG", "getLoginToken: " + url);
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    getLoginWithTokenFinished(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url, token);
    }

    private void getLoginWithTokenFinished(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        User us = null;
        if (jo.getBoolean("success")) {
            JSONObject response = jo.getJSONObject("response");
            String login = response.getString("username");
            String fName = response.getString("first_name");
            String email = response.getString("email");
            us = new User(login, fName, email);
        }
        delegate.whenWSIsTerminated(us);
    }

    public void getRegister(String login, String password, String email, DelegateAsyncTask delegate) {
        String url = "uc=main&action=register&login=" + login + "&password=" + password + "&email=" + email;
        Log.d("TAG", "getRegister: " + url);
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    getRegisterFinished(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void getRegisterFinished(String s, DelegateAsyncTask delegate) throws JSONException {
        JSONObject jo = new JSONObject(s);
        User us = null;
        if (jo.getBoolean("success")) {
            JSONObject response = jo.getJSONObject("response");
            String login = response.getString("username");
            String fName = response.getString("first_name");
            String email = response.getString("email");
            String token = response.getString("token");
            us = new User(login, fName, email);
            us.setToken(token);
        }
        delegate.whenWSIsTerminated(us);
    }


}
