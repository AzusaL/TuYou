package com.waibao.team.tuyou.callback;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.dto.JournalCommentDto;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Azusa on 2016/6/2.
 */
public abstract class CommentsCallback extends Callback<List<JournalCommentDto>> {
    @Override
    public List<JournalCommentDto> parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        List<JournalCommentDto> comments;
        String list_response = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(respon);
            list_response = jsonObject.get("list").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        comments = mapper.readValue(list_response, new TypeReference<List<JournalCommentDto>>() {
        });
        return comments;
    }
}
