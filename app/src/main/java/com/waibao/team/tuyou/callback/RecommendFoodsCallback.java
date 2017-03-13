package com.waibao.team.tuyou.callback;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.dto.FoodDto;
import com.waibao.team.tuyou.dto.JournalCommentDto;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Azusa on 2016/6/11.
 */
public abstract class RecommendFoodsCallback extends Callback<List<FoodDto>> {
    @Override
    public List<FoodDto> parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        List<FoodDto> foods;
        String list_response = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(respon);
            list_response = jsonObject.get("result").toString();
            Log.e("main", "parseNetworkResponse: "+list_response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        foods = mapper.readValue(list_response, new TypeReference<List<FoodDto>>() {
        });
        return foods;
    }
}
