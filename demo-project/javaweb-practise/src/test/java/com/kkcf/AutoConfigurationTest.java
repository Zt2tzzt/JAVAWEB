package com.kkcf;

import com.google.gson.Gson;
import com.kkcf.pojo.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AutoConfigurationTest {
    @Autowired
    private Gson gson;

    @Test
    public void testGson() {
        String jsonStr = gson.toJson(Result.success("哈哈"));
        System.out.println(jsonStr);
    }
}
