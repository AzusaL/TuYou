package com.waibao.team.tuyou.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Azusa on 2016/1/24.
 */
public class JsonUtils {

    //将一个类转换陈json字符串
    public static <T> String getJsonStringformat(T oject) {
        ObjectMapper mapper = new ObjectMapper();
        String JsonString = "";
        try {
            JsonString = mapper.writeValueAsString(oject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return JsonString;
        }
        return JsonString;
    }

    //将一个json字符串转成list
    public static ArrayList<String> getlistfromString(String string) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<String> list = new ArrayList<>();
        if (StringUtil.isEmpty(string)) {
            return list;
        }
        try {
            list = mapper.readValue(string, new TypeReference<ArrayList<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    //将一个Json字符串转换成对应的类
    public static <T> T getObjectfromString(String jsonString, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
