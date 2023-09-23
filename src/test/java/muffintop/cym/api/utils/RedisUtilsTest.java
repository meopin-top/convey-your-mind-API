package muffintop.cym.api.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class RedisUtilsTest {
    @Autowired
    private RedisUtils redisUtils;

    @Test
    @DisplayName("레디스 테스트")
    void setData() {
        String key ="test";
        String value = "test value";
        redisUtils.setData(key,value);

    }

    @Test
    @DisplayName("레디스 테스트2")
    void getData() {
        String key ="test";
        String value = "test value";
        String result = redisUtils.getData(key);
        assertTrue(value.equals(result));

    }

    @Test
    @DisplayName("레디스 테스트3")
    void deleteData() {
        String key ="test";
        redisUtils.deleteData(key);

    }
}