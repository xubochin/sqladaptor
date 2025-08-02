package com.sqladaptor.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.args.GeoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

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
}