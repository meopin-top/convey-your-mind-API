package muffintop.cym.api.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setData(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    public String getData(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }

}
