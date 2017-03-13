package com.waibao.team.tuyou.callback;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.dto.TrainListBean;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Azusa on 2016/6/4.
 * 火车车次信息
 */
public abstract class TrainListCallback extends Callback<List<TrainListBean>> {
    @Override
    public List<TrainListBean> parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        List<TrainListBean> list;
        String data = "";
        try {
            JSONObject object = new JSONObject(respon);
            String s = object.getString("data");
            data = new JSONObject(s).getString("trainList");
            Log.e("main", "parseNetworkResponse: "+data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        list = mapper.readValue(data, new TypeReference<List<TrainListBean>>() {
        });
        return list;
    }
}
