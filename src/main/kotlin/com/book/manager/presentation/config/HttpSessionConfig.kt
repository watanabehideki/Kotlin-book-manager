package com.book.manager.presentation.config

import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@EnableRedisHttpSession //SpringとRedisのセッション管理を有効にするアノテーション
class HttpSessionConfig {
    @Bean //＠BeanアノテーションによってJedisConnectionFactoryクラスのインスタンスを生成
    fun connectionFactory(): JedisConnectionFactory {
        /*
        ホスト名とポートを指定する場合(デフォルトはlocalhost, 6379)
        val redisStandaloneConfiguration = RedisStandaloneConfiguration().also {
            it.hostName = "任意のホスト名"
            it.port = "任意のポート番号"
        }
        return JedisConnectionFactory(redisStandaloneConfiguration)
        */
        return JedisConnectionFactory()
    }
}