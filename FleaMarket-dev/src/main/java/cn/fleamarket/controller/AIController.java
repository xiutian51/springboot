package cn.fleamarket.controller;


import cn.fleamarket.service.ProductService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/AI")
@Api("商品接口")
public class AIController {
    @Autowired
    ProductService productService;
    @PostMapping(value = "/getText", produces = "application/json")
    @ApiOperation("")
    public JSONObject getText(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String info = jsonObject.getString("info");
        System.out.println(info);
        return new JSONObject();
    }
}
