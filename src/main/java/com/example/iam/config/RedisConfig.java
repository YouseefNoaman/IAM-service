package com.example.iam.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuration class for Redis caching and connection settings.
 */
@Configuration
public final class RedisConfig {

  /**
   * Default cache TTL in seconds.
   */
  private static final int DEFAULT_CACHE_TTL = 60;

  /**
   * User cache TTL in seconds.
   */
  private static final int USER_CACHE_TTL = 30;

  /**
   * Auth token cache TTL in seconds.
   */
  private static final int AUTH_TOKEN_CACHE_TTL = 5;

  /**
   * Redis host from configuration.
   */
  @Value("${spring.redis.host}")
  private String redisHost;

  /**
   * Redis port from configuration.
   */
  @Value("${spring.redis.port}")
  private int redisPort;

  /**
   * Creates Redis connection factory.
   *
   * @return the Redis connection factory
   */
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration config =
        new RedisStandaloneConfiguration(redisHost, redisPort);
    return new LettuceConnectionFactory(config);
  }

  /**
   * Creates Redis template for key-value operations.
   *
   * @param connectionFactory the Redis connection factory
   * @return the configured Redis template
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      final RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
  }

  /**
   * Creates cache manager with TTL configurations.
   *
   * @param connectionFactory the Redis connection factory
   * @return the configured cache manager
   */
  @Bean
  public CacheManager cacheManager(final RedisConnectionFactory connectionFactory) {
    RedisCacheConfiguration defaultConfig = RedisCacheConfiguration
        .defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(DEFAULT_CACHE_TTL));

    Map<String, RedisCacheConfiguration> configs = new HashMap<>();

    configs.put("users", RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(USER_CACHE_TTL)));

    configs.put("authTokens", RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(AUTH_TOKEN_CACHE_TTL)));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(defaultConfig)
        .withInitialCacheConfigurations(configs)
        .build();
  }
}
