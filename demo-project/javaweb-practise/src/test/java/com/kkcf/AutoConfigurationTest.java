package com.kkcf;

import com.example.HeaderParser;
import com.example.TokenParser;
import com.google.gson.Gson;
import com.kkcf.pojo.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class AutoConfigurationTest {
    @Autowired
    private Gson gson;
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testGson() {
        String jsonStr = gson.toJson(Result.success("哈哈"));
        System.out.println(jsonStr); // {"code":1,"msg":"success","data":"哈哈"}
    }

    @Test
    public void testTokenParse() {
        System.out.println(applicationContext.getBean(TokenParser.class));
    }

    @Test
    public void testHeaderParser() {
        System.out.println(applicationContext.getBean(HeaderParser.class));
    }
}
