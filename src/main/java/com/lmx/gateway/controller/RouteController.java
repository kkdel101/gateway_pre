package com.lmx.gateway.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lmx.gateway.loader.RouteLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteLoader routeLoader;


    @GetMapping("/add")
    public JSONObject addRoute(){
        String id = routeLoader.addRoute(UUID.randomUUID().toString(), "/test/getName", "/getGender", "http://localhost:8089");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",id);
        return jsonObject;
    }

    @GetMapping("/routeList")
    public JSONObject routeList(){
        JSONObject result = new JSONObject();
        result.put("data",routeLoader.getAllRoutes());
        return result;
    }




}
