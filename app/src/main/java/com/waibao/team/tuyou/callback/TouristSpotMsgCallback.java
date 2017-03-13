package com.waibao.team.tuyou.callback;

import android.util.Log;

import com.waibao.team.tuyou.dto.TouristSpotDto;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Azusa on 2016/6/3.
 * 景点信息
 */
public abstract class TouristSpotMsgCallback extends Callback<List<TouristSpotDto>> {
    @Override
    public List<TouristSpotDto> parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        Log.e("main", "parseNetworkResponse: "+respon);
        List<TouristSpotDto> touristSpotDtos = new ArrayList<>();
        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject = new JSONObject(respon);
            jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                String title, grade, price_min, address, imgurl, url;
                TouristSpotDto touristSpotDto;
                JSONObject subObject = (JSONObject) jsonArray.get(i);

                title = subObject.getString("title");
                grade = subObject.getString("grade");
                price_min = subObject.getString("price_min");
                address = subObject.getString("address");
                imgurl = subObject.getString("imgurl");
                url = subObject.getString("url");

                touristSpotDto = new TouristSpotDto(title,grade,price_min,address,imgurl,url);
                touristSpotDtos.add(touristSpotDto);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return touristSpotDtos;
    }
}
