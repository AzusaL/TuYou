package com.waibao.team.tuyou.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.dto.Distance;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Azusa on 2016/6/3.
 * 酒店信息
 */
public abstract class DistanceCallback extends Callback<Float> {
    @Override
    public Float parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        Distance distance;
        ObjectMapper mapper = new ObjectMapper();
        distance = mapper.readValue(respon, Distance.class);
        float i = distance.getResult().getElements().get(0).getDistance().getValue();
        return i/1000;
    }
}
