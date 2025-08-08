import redis.clients.jedis.Jedis;

public class SimpleHashTest {
    public static void main(String[] args) {
        System.out.println("[SIMPLE TEST] Starting simple hash test...");
        
        try {
            // 连接到本地Redis服务器
            Jedis jedis = new Jedis("127.0.0.1", 6379);
            jedis.connect();
            
            System.out.println("[SIMPLE TEST] Connected to Redis server");
            
            // 测试PING命令
            String pingResult = jedis.ping();
            System.out.println("[SIMPLE TEST] PING result: " + pingResult);
            
            // 测试HSET命令
            System.out.println("[SIMPLE TEST] Executing HSET command...");
            Long hsetResult = jedis.hset("test:hash", "field1", "value1");
            System.out.println("[SIMPLE TEST] HSET result: " + hsetResult);
            
            // 测试HGET命令
            System.out.println("[SIMPLE TEST] Executing HGET command...");
            String hgetResult = jedis.hget("test:hash", "field1");
            System.out.println("[SIMPLE TEST] HGET result: " + hgetResult);
            
            // 清理
            jedis.del("test:hash");
            jedis.close();
            
            System.out.println("[SIMPLE TEST] Test completed successfully");
            
        } catch (Exception e) {
            System.err.println("[SIMPLE TEST] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}