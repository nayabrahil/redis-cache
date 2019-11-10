package com.example.readiscache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.host}")
    private String redisHost;

    /*@Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(
                this.redisHost,
//                Integer.parseInt(this.redisPort)
                this.redisPort
                );
    }*/

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration());
    }

    /*@Bean
    public RedisTemplate<?, ?> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //template.setKeySerializer(new GenericToStringSerializer<Long>(Long.class));

        return template;
    }*/

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = new RedisServer(this.redisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}