package com.waibao.team.tuyou.callback;

import android.util.Log;

import com.waibao.team.tuyou.dto.HotelMsgDto;
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
 * 酒店信息
 */
public abstract class HotelMsgCallback extends Callback<List<HotelMsgDto>> {
    @Override
    public List<HotelMsgDto> parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        Log.e("main", "parseNetworkResponse: " + respon);
        List<HotelMsgDto> hotelMsgDtos = new ArrayList<>();
        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject = new JSONObject(respon);
            jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                String name, phone, address, rating, price, detail_url;
                double lat, lng;
                HotelMsgDto hotelMsgDto;
                JSONObject subObject = (JSONObject) jsonArray.get(i);
                if (subObject.getString("detail").equals("0")) {
                    continue;
                }
                name = subObject.getString("name");
                if (subObject.isNull("telephone")) {
                    continue;
                } else {
                    phone = subObject.getString("telephone");
                }
                address = subObject.getString("address");

                JSONObject detail = subObject.getJSONObject("detail_info");
                detail_url = detail.getString("detail_url");
                price = detail.getString("price");
                rating = detail.getString("overall_rating");

                JSONObject location = subObject.getJSONObject("location");
                lat = location.getDouble("lat");
                lng = location.getDouble("lng");
                hotelMsgDto = new HotelMsgDto(name, price, detail_url, phone, address, rating, lat, lng);
                hotelMsgDtos.add(hotelMsgDto);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hotelMsgDtos;
    }
}
