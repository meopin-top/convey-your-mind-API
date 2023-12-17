package muffintop.cym.api.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import muffintop.cym.api.domain.RedisProjectEntity;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("레디스 테스트")
    void setData() throws JsonProcessingException {
        Long key = 5L;
        RedisProjectEntity value = RedisProjectEntity.builder()
            .projectId(key.toString())
            .status('R')
            .contents(new ArrayList<>())
            .build();

        redisUtils.setData(key.toString(), objectMapper.writeValueAsString(value));

    }

    @Test
    @DisplayName("레디스 테스트2")
    void getData() {
        String key = "test";
        String value = "test value";
        String result = redisUtils.getData(key);
        assertTrue(value.equals(result));

    }

    @Test
    @DisplayName("레디스 테스트3")
    void deleteData() {
        String key = "test";
        redisUtils.deleteData(key);

    }
}