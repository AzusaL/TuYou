package com.waibao.team.tuyou.callback;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.dto.UserDto;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Delete_exe on 2016/6/4.
 */
public abstract class RecommendFriendsCallback extends Callback<List<UserDto>> {
    private int type;

    public RecommendFriendsCallback(int type) {
        this.type = type;
    }

    @Override
    public List<UserDto> parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        List<UserDto> list;
        String list_response = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(respon);
            String field = type == 1 ? "kmList" : "timesList";
            list_response = jsonObject.get(field).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        list = mapper.readValue(list_response, new TypeReference<List<UserDto>>() {
        });
        return list;
    }
}
