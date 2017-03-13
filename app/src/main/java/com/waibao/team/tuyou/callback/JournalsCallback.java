package com.waibao.team.tuyou.callback;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.dto.JournalDto;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Azusa on 2016/6/2.
 */
public abstract class JournalsCallback extends Callback<List<JournalDto>> {
    @Override
    public List<JournalDto> parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        List<JournalDto> journalDtos;
        String list_response = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(respon);
            list_response = jsonObject.get("list").toString();
            Log.e("main", "parseNetworkResponse: "+list_response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        journalDtos = mapper.readValue(list_response, new TypeReference<List<JournalDto>>() {
        });
        return journalDtos;
    }
}
