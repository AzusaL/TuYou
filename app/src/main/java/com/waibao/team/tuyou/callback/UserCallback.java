package com.waibao.team.tuyou.callback;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.model.User;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Delete_exe on 2016/6/2.
 */
public abstract class UserCallback extends Callback<User> {
    @Override
    public User parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        Log.e("main", "parseNetworkResponse: " + respon);
        User user;
        String list_response = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(respon);
            list_response = jsonObject.get("userDto").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        user = mapper.readValue(list_response, User.class);
        return user;
    }
}
