package com.waibao.team.tuyou.callback;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.JournalDto;
import com.waibao.team.tuyou.model.User;
import com.waibao.team.tuyou.utils.StringUtil;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Azusa on 2016/6/2.
 */
public abstract class SearchResultCallback extends Callback<HashMap<String, Object>> {
    @Override
    public HashMap<String, Object> parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        Log.e("main", "parseNetworkResponse: "+respon);
        List<User> userDtos = new ArrayList<>();
        List<GroupDto> groupDtos = new ArrayList<>();
        List<JournalDto> journalDtos = new ArrayList<>();
        HashMap<String, Object> maps = new HashMap<>();
        String list_peopel;
        String list_group;
        String list_journal;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(respon);
            ObjectMapper mapper1 = new ObjectMapper();
            ObjectMapper mapper2 = new ObjectMapper();
            ObjectMapper mapper3 = new ObjectMapper();

            if (!jsonObject.isNull("userDtoList")) {
                list_peopel = jsonObject.get("userDtoList").toString();
                if (!StringUtil.isEmpty(list_peopel)) {
                    userDtos = mapper1.readValue(list_peopel, new TypeReference<List<User>>() {
                    });
                }
            }

            if (!jsonObject.isNull("groupDtoList")) {
                list_group = jsonObject.get("groupDtoList").toString();
                if (!StringUtil.isEmpty(list_group)) {
                    groupDtos = mapper2.readValue(list_group, new TypeReference<List<GroupDto>>() {
                    });
                }
            }

            if (!jsonObject.isNull("journalDtoList")) {
                list_journal = jsonObject.get("journalDtoList").toString();
                if (!StringUtil.isEmpty(list_journal)) {
                    journalDtos = mapper3.readValue(list_journal, new TypeReference<List<JournalDto>>() {
                    });
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        maps.put("user", userDtos);
        maps.put("group", groupDtos);
        maps.put("journal", journalDtos);
        return maps;
    }
}
