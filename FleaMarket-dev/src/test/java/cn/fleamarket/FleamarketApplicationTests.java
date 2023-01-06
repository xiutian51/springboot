package cn.fleamarket;

import cn.fleamarket.service.UserService;
import cn.fleamarket.utils.LoginUtil;
import cn.fleamarket.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class FleamarketApplicationTests {


    @Autowired
    private LoginUtil loginUtil;

    @Autowired
    private UserService userService;


    @Autowired
    private RedisUtil<String, List<Integer>> redisUtil;

    @Test
    void contextLoads() {
    }

}
