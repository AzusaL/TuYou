package com.waibao.team.tuyou.callback;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.dto.GroupDto;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Delete_exe on 2016/6/4.
 */
public abstract class HomeGroupCallback extends Callback<List<GroupDto>>{
    public List<GroupDto> parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        List<GroupDto> list;
        String list_response = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(respon);
            list_response = jsonObject.get("groupDtoList").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        list = mapper.readValue(list_response, new TypeReference<List<GroupDto>>() {
        });
        return list;
    }
}
