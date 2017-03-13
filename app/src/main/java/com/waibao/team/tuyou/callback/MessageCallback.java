package com.waibao.team.tuyou.callback;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.dto.MessageDto;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Delete_exe on 2016/6/3.
 */
public abstract class MessageCallback extends Callback<List<MessageDto>> {
    @Override
    public List<MessageDto> parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        List<MessageDto> list;
        String list_response = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(respon);
            list_response = jsonObject.get("MessageDto").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (null == list_response) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        list = mapper.readValue(list_response, new TypeReference<List<MessageDto>>() {
        });
        return list;
    }
}
