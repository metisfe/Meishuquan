package com.metis.base.fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * Created by Beak on 2015/9/24.
 */
public class PlayerProperty implements Serializable {

    private static final String TAG = PlayerProperty.class.getSimpleName();

    private static PlayerProperty sProperty = null;

    public static PlayerProperty getInstance (Context context) {
        if (sProperty == null) {
            AssetManager assetManager = context.getAssets();
            try {
                InputStream is = assetManager.open("player.property");
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String str = null;
                StringBuilder sb = new StringBuilder();
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                if (sb != null) {
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    sProperty = new PlayerProperty();
                    sProperty.setUser_id(jsonObject.getString("user_id"));
                    sProperty.setApi_key(jsonObject.getString("api_key"));
                }

                Log.v(TAG, "getInstance " + sb);
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("no file named player.property under assets or empty player.property file");
            } catch (JSONException e) {
                e.printStackTrace();
                throw new Error("no file named player.property under assets or empty player.property file");
            }
        }
        return sProperty;
    }

    private String user_id;
    private String api_key;

    private PlayerProperty () {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }
}
