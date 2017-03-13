package com.waibao.team.tuyou.callback;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.dto.UserDto;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Delete_exe on 2016/6/4.
 */
public abstract class HomePeopleCallback extends Callback<List<UserDto>> {
    @Override
    public List<UserDto> parseNetworkResponse(Response response) throws IOException {
        String respon = response.body().string();
        List<UserDto> listkm = new ArrayList<>();
        List<UserDto> listtime;
        String listkm_response = null;
        String listtime_response = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(respon);
            listkm_response = jsonObject.get("kmList").toString();
            listtime_response = jsonObject.get("timesList").toString();
            ObjectMapper mapper = new ObjectMapper();
            listkm = mapper.readValue(listkm_response, new TypeReference<List<UserDto>>() {
            });
            listtime = mapper.readValue(listtime_response, new TypeReference<List<UserDto>>() {
            });
            listkm.addAll(listtime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        catch (JsonParseException e) {
//            Log.e("LLLLL", e.toString());
//        } catch (JsonMappingException e) {
//            Log.e("LLLLL", e.toString());
//        } catch (IOException e) {
//            Log.e("LLLLL", e.toString());
//        }

        return listkm;
    }
}
