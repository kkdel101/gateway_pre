package com.lmx.gateway.controller;


import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/getGender")
    public JSONObject getGender(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sex","男");
        return jsonObject;
    }


}
