package cn.fleamarket.controller;

import java.util.*;


import cn.fleamarket.domain.Product;
import cn.fleamarket.domain.User;
import cn.fleamarket.service.MessageService;
import cn.fleamarket.service.ProductService;
import cn.fleamarket.service.UserService;
import cn.fleamarket.service.impl.KeyService;
import cn.fleamarket.utils.StringTool;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.ScriptOutputType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 商品表
 *
 * @author zining
 * @email ${email}
 * @date 2019-11-12 10:46:22
 */
@RestController
@RequestMapping("/question")
@Api("商品接口")
public class QuestionController {
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    KeyService keyService;
    static HashMap<String,int[]> answerMap = new HashMap<>();

    static HashMap<String,Double> scoreMap = new HashMap<>();
    static {
        answerMap.put("1",new int[]{1,2,2,2,4,2});
        answerMap.put("2",new int[]{1,1,1,3,2});
        answerMap.put("3",new int[]{3,1,2,3,2,1});
        answerMap.put("4",new int[]{3,4,3,1,3,4});
        answerMap.put("5",new int[]{2,4,1,4,4,1});
    }
    static int SCORE_PER_QUESTION = 20;


    @PostMapping(value = "/postAnswer", produces = "application/json")
    @ApiOperation("分页查询属于某个用户的商品列表,就是我发布的商品,入参是page:第几页,number:每页几条")
    public JSONObject postAnswer(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        System.out.println("已经收到数据!!!");
        String userName = jsonObject.getString("username");
        System.out.println("用户名是:"+userName);
        System.out.println("index是:"+jsonObject.getString("index"));
        List<Integer> list = jsonObject.getJSONArray("data").toJavaList(Integer.class);
        System.out.println("答案是:"+ JSON.toJSONString(list));
        int[] answer = answerMap.get(jsonObject.getString("index"));
        int score = 0;
        for (int i = 0; i < answer.length; i++) {
            if(answer[i] == list.get(i)+1){
                score += SCORE_PER_QUESTION;
            }
        }
        JSONObject ret = new JSONObject();
        ret.put("data",score);
        System.out.println(ret.toJSONString());
        scoreMap.put(userName,scoreMap.getOrDefault(userName,0d)+score);
        return ret;
    }


    @PostMapping(value = "/scoreById", produces = "application/json")
    @ApiOperation("分页查询属于某个用户的商品列表,就是我发布的商品,入参是page:第几页,number:每页几条")
    public JSONObject scoreById(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String userName = jsonObject.getString("username");
        System.out.println("用户名是:"+userName);
        JSONObject ret = new JSONObject();
        System.out.println(ret.toJSONString());
        double score = scoreMap.getOrDefault(userName,0d);
        ret.put("data",score);
        return ret;
    }

    @PostMapping(value = "/buyProduct", produces = "application/json")
    @ApiOperation("购买商品")
    public JSONObject buyProduct(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String userName = jsonObject.getString("username");
        System.out.println("用户名是:"+userName);
        String pId = jsonObject.getString("pid");
        System.out.println("pId is"+pId);
        JSONObject ret = new JSONObject();
        double score = scoreMap.getOrDefault(userName,0d);
        System.out.println("score is:"+score);
        Product product = productService.selectById(pId);
        double price = product.getPrice();
        if(score >= price){
            ret.put("data",true);
            score -= price;
            scoreMap.put(userName,score);
        }
        else {
            ret.put("data",false);
        }
        return ret;
    }

    @GetMapping(value = "/getChat")
    @ApiOperation("分页查询属于某个用户的商品列表,就是我发布的商品,入参是page:第几页,number:每页几条")
    public String getChat(String username) {
        return keyService.getChat(username);
    }

}
