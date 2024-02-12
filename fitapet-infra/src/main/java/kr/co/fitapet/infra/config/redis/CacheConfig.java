package kr.co.fitapet.infra.config.redis;

import kr.co.fitapet.infra.common.annotation.InfraRedisConnectionFactory;
import kr.co.fitapet.infra.common.annotation.ManagerCacheManager;
import kr.co.fitapet.infra.common.annotation.OidcCacheManager;
import kr.co.fitapet.infra.common.annotation.SecurityUserCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableCaching
public class CacheConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    @InfraRedisConnectionFactory
    public RedisConnectionFactory infraRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
//        config.setPassword(); // redis 패스워드 설정 시, 주석 해제
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder().build();
        return new LettuceConnectionFactory(config, clientConfig);
    }

    @Bean
    @SecurityUserCacheManager
    public CacheManager securityUserCacheManager(@InfraRedisConnectionFactory RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(3))
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
                );
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = Map.of("sercurityConfig", config);

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
    }

    @Bean
    @ManagerCacheManager
    public CacheManager managerCacheManager(@InfraRedisConnectionFactory RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(3))
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
                );
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = Map.of("managerConfig", config);

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
    }

    @Bean
    @OidcCacheManager
    public CacheManager oidcCacheManger(@InfraRedisConnectionFactory RedisConnectionFactory cf) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new StringRedisSerializer()
                        ))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()
                        ))
                .entryTtl(Duration.ofDays(3));

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cf).cacheDefaults(config).build();
    }
}