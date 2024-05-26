package com.expensecalculator.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {
	
	private static JedisPool pool = null;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(128);  
        config.setMaxIdle(128);    
        config.setMinIdle(16);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        pool = new JedisPool(config, "localhost", 6379);
    }

    public static JedisPool getPool() {
        return pool;
    }
}
