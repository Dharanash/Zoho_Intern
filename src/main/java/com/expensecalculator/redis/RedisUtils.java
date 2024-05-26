package com.expensecalculator.redis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RedisUtils{
	public static boolean isCurrentMonthTransaction(String transactionDateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime transactionDateTime = LocalDateTime.parse(transactionDateTimeString, formatter);
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

        return transactionDateTime.getYear() == now.getYear() && transactionDateTime.getMonth() == now.getMonth();
    }

    public static void deleteCache(String redisKey) {
        try (Jedis jedis = RedisConnection.getPool().getResource()) {
            jedis.del(redisKey);
        }
    }
    
    public static void addToCacheHset(String cacheKey, HashMap<String, String> transactionCategoryMap) {
        try (Jedis jedis = RedisConnection.getPool().getResource()) {
        	jedis.watch(cacheKey);
        	
        	Transaction transaction = jedis.multi();

            for (Map.Entry<String, String> entry : transactionCategoryMap.entrySet()) {
                transaction.hset(cacheKey, entry.getKey(), entry.getValue());
            }
            transaction.expire(cacheKey, 3600);
            transaction.exec();
            jedis.unwatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void addToCacheHset(String cacheKey, int categoryId, String category) {
        try (Jedis jedis = RedisConnection.getPool().getResource()) {
            jedis.hset(cacheKey, String.valueOf(categoryId), category);
        }
    }
    
    public static void removeFromCacheHset(String cacheKey) {
        try (Jedis jedis = RedisConnection.getPool().getResource()) {
            jedis.hdel(cacheKey);
        }
    }
}
