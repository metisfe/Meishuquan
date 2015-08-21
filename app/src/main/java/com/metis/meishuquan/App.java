package com.metis.meishuquan;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.User;
import com.metis.msnetworklib.contract.ReturnInfo;

/**
 * Created by gaoyunfei on 15/6/30.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*AccountManager.getInstance(this).login("15755555555", "111111", new RequestCallback<User>() {
            @Override
            public void callback(ReturnInfo<User> returnInfo, String callbackId) {
                Toast.makeText(App.this, returnInfo.isSuccess() ? "login success Teacher" : "login failed", Toast.LENGTH_SHORT).show();
            }
        });*/
        /*AccountManager.getInstance(this).login("15744444444", "111111", new RequestCallback<User>(){

            @Override
            public void callback(ReturnInfo<User> returnInfo, String callbackId) {

            }
        });*/
        /*AccountManager.getInstance(this).login("15701218034", "12345678", new RequestCallback<User>() {
            @Override
            public void callback(ReturnInfo<User> returnInfo, String callbackId) {
                Toast.makeText(App.this, returnInfo.isSuccess() ? "login success Student" : "login failed", Toast.LENGTH_SHORT).show();
            }
        });*/

        /*AccountManager.getInstance(this).login("15777777777", "111111", new RequestCallback<User>() {
            @Override
            public void callback(ReturnInfo<User> returnInfo, String callbackId) {
                Toast.makeText(App.this, returnInfo.isSuccess() ? "login success Studio" : "login failed", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}