package com.fleettrack.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(10);
    private static final Duration SINGLE_TTL  = Duration.ofMinutes(15);

    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration defaultConfig =
                buildCacheConfig(DEFAULT_TTL);

        Map<String, RedisCacheConfiguration> cacheConfigs = Map.of(
                "vehicles", buildCacheConfig(DEFAULT_TTL),
                "vehicle",  buildCacheConfig(SINGLE_TTL),
                "drivers",  buildCacheConfig(DEFAULT_TTL),
                "driver",   buildCacheConfig(SINGLE_TTL)
        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringSerializer =
                new StringRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer());
        template.setHashValueSerializer(jsonSerializer());
        template.afterPropertiesSet();

        return template;
    }

    private RedisCacheConfiguration buildCacheConfig(Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .disableCachingNullValues()
                .serializeKeysWith(
                        SerializationPair.fromSerializer(
                                new StringRedisSerializer()))
                .serializeValuesWith(
                        SerializationPair.fromSerializer(
                                jsonSerializer()));
    }

    private RedisSerializer<Object> jsonSerializer() {
        return RedisSerializer.json();
    }
}