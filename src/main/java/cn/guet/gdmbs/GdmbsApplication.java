package cn.guet.gdmbs;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@MapperScan(basePackages = "cn.guet.gdmbs.cn.guet.gdmbs.mapper")
public class GdmbsApplication {
    public static void main(String[] args) {

        SpringApplication.run(GdmbsApplication.class, args);

    }

    /*@Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        StringRedisTemplate template=new StringRedisTemplate();
        Set<String> keys = template.keys("*");
        template.delete(keys);
        System.out.println("cccccccccccccccc");
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }*/


    @Bean
    public Redisson redisson(){
        //此为单机模式
        Config config=new Config();
        config.useSingleServer().setAddress("redis://localhost:6379").setDatabase(0);
        return (Redisson)Redisson.create(config);
    }
}
