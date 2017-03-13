package com.waibao.team.tuyou.callback;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.dto.VoteDto;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Delete_exe on 2016/6/9.
 */
public abstract class AllVoteCallback extends Callback<List<VoteDto>> {
    @Override
    public List<VoteDto> parseNetworkResponse(Response response) throws IOException {
        String respone = response.body().string();
        List<VoteDto> VoteDtos = new ArrayList<>();
        String list_response = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(respone);
            list_response = jsonObject.get("list").toString();
            Log.e("LLLLL", list_response);
            ObjectMapper mapper = new ObjectMapper();
            VoteDtos = mapper.readValue(list_response, new TypeReference<List<VoteDto>>() {
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return VoteDtos;
    }
}
