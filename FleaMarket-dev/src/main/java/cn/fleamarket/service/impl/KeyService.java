package cn.fleamarket.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class KeyService {
    private static Integer TIME_OUT = 30;
    private static String model = "text-davinci-003";
    private static Double temperature = 0.9;
    private static Integer max_tokens = 150;
    private static Integer top_p = 1;
    private static Double frequency_penalty = 0.0;
    private static Double presence_penalty = 0.6;

    public String getChat(String request){
        String ret =this.getText(this.getResponse(request,"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"));
        System.out.println(ret);
        return ret;
    }


    public Response getResponse(String apiRequest, String key) {
        //todo 后期换成httpclient
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(TIME_OUT, TimeUnit.SECONDS);
        client.setReadTimeout(TIME_OUT,TimeUnit.SECONDS);
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject json = new JSONObject();
        try {
            json.put("model", model);
            json.put("prompt", apiRequest);
            json.put("temperature", temperature);
            json.put("max_tokens", max_tokens);
            json.put("top_p", top_p);
            json.put("frequency_penalty", frequency_penalty);
            json.put("presence_penalty", presence_penalty);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        key ="Bearer "+key;
        RequestBody body = RequestBody.create(mediaType, String.valueOf(json));
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .method("POST", body)
                .addHeader("Authorization", key)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(response));
        return response;
    }

    public String getText(Response response) {
        String responseData = null;
        try {
            responseData = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(responseData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String ret = null;
        if(jsonObject.containsKey("choices")) {
            ret = jsonObject.getJSONArray("choices").getJSONObject(0).getString("text");
            int index = 0;
            while(index+3<ret.length()){
                if(ret.charAt(index) == '\n' ){
                    break;
                }
                index++;
            }
            ret = ret.substring(index+2).trim();
        }
        //当密钥的余额不足时
        return ret;
    }
}
