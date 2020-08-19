package cn.guet.gdmbs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CacheUpdateRunner implements CommandLineRunner {

    public static final String CACHE_KEY_PREFIX = "spring:";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        String pattern = CACHE_KEY_PREFIX + "*";
        RedisConnection connection = redisTemplate
                .getConnectionFactory().getConnection();

        Set<byte[]> caches = connection.keys(pattern.getBytes());
        if(!caches.isEmpty()){
            connection.del(caches.toArray(new byte[][]{}));
        }
    }

}