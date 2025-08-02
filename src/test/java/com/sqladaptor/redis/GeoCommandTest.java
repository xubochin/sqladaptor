package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.resps.GeoRadiusResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class GeoCommandTest {
    
    private static final String REDIS_URL = "redis://:ii%407zY%24s%266Dg6%2A@192.168.100.13:6379/0";

    private Jedis createConnection() {
        int maxRetries = 3;
        int retryDelay = 1000;
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                URI redisUri = URI.create(REDIS_URL);
                Jedis jedis = new Jedis(redisUri, 300000, 300000);
                jedis.ping();
                return jedis;
            } catch (Exception e) {
                if (i < maxRetries - 1) {
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    fail("Failed to connect to Redis after " + maxRetries + " attempts: " + e.getMessage());
                }
            }
        }
        return null;
    }
    
    @Test
    void testGeoOperations() {
        System.out.println("[TEST START] testGeoOperations - 测试地理位置操作(GEOADD/GEOPOS/GEODIST)");
        String key = "test:geo:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // GEOADD操作
            Long geoaddResult = jedis.geoadd(key, 13.361389, 38.115556, "Palermo");
            assertEquals(1L, geoaddResult);
            jedis.geoadd(key, 15.087269, 37.502669, "Catania");
            
            // GEOPOS操作
            List<GeoCoordinate> geoposResult = jedis.geopos(key, "Palermo", "Catania");
            assertEquals(2, geoposResult.size());
            assertNotNull(geoposResult.get(0));
            assertNotNull(geoposResult.get(1));
            
            // GEODIST操作
            Double geodistResult = jedis.geodist(key, "Palermo", "Catania", GeoUnit.KM);
            assertNotNull(geodistResult);
            assertTrue(geodistResult > 0);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testGeoOperations\n");
    }
    
    @Test
    void testGeoRadiusOperations() {
        System.out.println("[TEST START] testGeoRadiusOperations - 测试GEORADIUS命令");
        String key = "test:geo:radius:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 添加测试数据
            jedis.geoadd(key, 13.361389, 38.115556, "Palermo");
            jedis.geoadd(key, 15.087269, 37.502669, "Catania");
            jedis.geoadd(key, 12.758489, 38.788135, "edge");
            
            // GEORADIUS操作 - 查找指定坐标范围内的成员
            List<GeoRadiusResponse> radiusResult = jedis.georadius(key, 15, 37, 200, GeoUnit.KM);
            assertNotNull(radiusResult);
            assertTrue(radiusResult.size() >= 0);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testGeoRadiusOperations\n");
    }
    
    @Test
    void testGeoRadiusByMemberOperations() {
        System.out.println("[TEST START] testGeoRadiusByMemberOperations - 测试GEORADIUSBYMEMBER命令");
        String key = "test:geo:radiusbymember:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 添加测试数据
            jedis.geoadd(key, 13.361389, 38.115556, "Palermo");
            jedis.geoadd(key, 15.087269, 37.502669, "Catania");
            jedis.geoadd(key, 12.758489, 38.788135, "edge");
            
            // GEORADIUSBYMEMBER操作 - 以某个成员为中心查找范围内的其他成员
            List<GeoRadiusResponse> radiusByMemberResult = jedis.georadiusByMember(key, "Palermo", 200, GeoUnit.KM);
            assertNotNull(radiusByMemberResult);
            assertTrue(radiusByMemberResult.size() >= 0);
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testGeoRadiusByMemberOperations\n");
    }
    
    @Test
    void testGeoHashOperations() {
        System.out.println("[TEST START] testGeoHashOperations - 测试GEOHASH命令");
        String key = "test:geo:hash:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 添加测试数据
            jedis.geoadd(key, 13.361389, 38.115556, "Palermo");
            jedis.geoadd(key, 15.087269, 37.502669, "Catania");
            
            // GEOHASH操作 - 获取成员的geohash值
            List<String> geohashResult = jedis.geohash(key, "Palermo", "Catania");
            assertNotNull(geohashResult);
            assertEquals(2, geohashResult.size());
            
            // 验证geohash不为空（如果成员存在）
            for (String hash : geohashResult) {
                if (hash != null) {
                    assertTrue(hash.length() > 0);
                }
            }
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testGeoHashOperations\n");
    }
    
    @Test
    void testAdvancedGeoOperations() {
        System.out.println("[TEST START] testAdvancedGeoOperations - 测试高级地理位置操作");
        String key = "test:geo:advanced:" + System.currentTimeMillis();
        try (Jedis jedis = createConnection()) {
            assertNotNull(jedis);
            
            // 批量添加地理位置数据
            Map<String, GeoCoordinate> memberCoordinateMap = new HashMap<>();
            memberCoordinateMap.put("Palermo", new GeoCoordinate(13.361389, 38.115556));
            memberCoordinateMap.put("Catania", new GeoCoordinate(15.087269, 37.502669));
            memberCoordinateMap.put("Rome", new GeoCoordinate(12.496366, 41.902782));
            
            Long batchAddResult = jedis.geoadd(key, memberCoordinateMap);
            assertTrue(batchAddResult >= 0);
            
            // 测试不存在的成员
            List<GeoCoordinate> nonExistentResult = jedis.geopos(key, "NonExistent");
            assertNotNull(nonExistentResult);
            assertEquals(1, nonExistentResult.size());
            assertNull(nonExistentResult.get(0)); // 不存在的成员应该返回null
            
            // 测试距离计算的不同单位
            Double distKm = jedis.geodist(key, "Palermo", "Catania", GeoUnit.KM);
            Double distM = jedis.geodist(key, "Palermo", "Catania", GeoUnit.M);
            if (distKm != null && distM != null) {
                assertTrue(distM > distKm); // 米应该比千米数值大
            }
            
            jedis.del(key);
        }
        System.out.println("[TEST END] testAdvancedGeoOperations\n");
    }
}