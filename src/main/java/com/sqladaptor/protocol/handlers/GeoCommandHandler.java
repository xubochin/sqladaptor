package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 处理 Redis GEO（地理位置）命令的处理器
 * 支持的命令：GEOADD, GEOPOS, GEODIST, GEORADIUS, GEORADIUSBYMEMBER, GEOHASH
 */
public class GeoCommandHandler extends BaseCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(GeoCommandHandler.class);
    
    // 模拟地理位置数据存储
    private final Map<String, Map<String, GeoLocation>> geoData = new HashMap<>();
    
    public GeoCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        super(databaseManager, converter);
    }
    
    @Override
    public String handle(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();
        
        logger.debug("Processing GEO command: {} with {} arguments", command, args.size());
        
        switch (command) {
            case "GEOADD":
                return handleGeoAdd(args);
            case "GEOPOS":
                return handleGeoPos(args);
            case "GEODIST":
                return handleGeoDist(args);
            case "GEORADIUS":
                return handleGeoRadius(args);
            case "GEORADIUSBYMEMBER":
                return handleGeoRadiusByMember(args);
            case "GEOHASH":
                return handleGeoHash(args);
            default:
                logger.warn("Unsupported GEO command: {}", command);
                return "-ERR Unknown GEO command '" + command + "'\r\n";
        }
    }
    
    /**
     * 处理 GEOADD 命令
     * 格式：GEOADD key longitude latitude member [longitude latitude member ...]
     */
    private String handleGeoAdd(List<String> args) {
        if (args.size() < 4 || (args.size() - 1) % 3 != 0) {
            return "-ERR wrong number of arguments for 'geoadd' command\r\n";
        }
        
        String key = args.get(0);
        int added = 0;
        
        // 确保key存在于geoData中
        geoData.putIfAbsent(key, new HashMap<>());
        Map<String, GeoLocation> locations = geoData.get(key);
        
        // 处理每组经纬度和成员
        for (int i = 1; i < args.size(); i += 3) {
            try {
                double longitude = Double.parseDouble(args.get(i));
                double latitude = Double.parseDouble(args.get(i + 1));
                String member = args.get(i + 2);
                
                // 验证经纬度范围
                if (longitude < -180 || longitude > 180) {
                    return "-ERR invalid longitude\r\n";
                }
                if (latitude < -85.05112878 || latitude > 85.05112878) {
                    return "-ERR invalid latitude\r\n";
                }
                
                // 如果成员不存在则计数
                if (!locations.containsKey(member)) {
                    added++;
                }
                
                locations.put(member, new GeoLocation(longitude, latitude));
            } catch (NumberFormatException e) {
                return "-ERR value is not a valid float\r\n";
            }
        }
        
        return ":" + added + "\r\n";
    }
    
    /**
     * 处理 GEOPOS 命令
     * 格式：GEOPOS key member [member ...]
     */
    private String handleGeoPos(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'geopos' command\r\n";
        }
        
        String key = args.get(0);
        Map<String, GeoLocation> locations = geoData.get(key);
        
        StringBuilder response = new StringBuilder();
        response.append("*").append(args.size() - 1).append("\r\n");
        
        for (int i = 1; i < args.size(); i++) {
            String member = args.get(i);
            GeoLocation location = locations != null ? locations.get(member) : null;
            
            if (location != null) {
                response.append("*2\r\n");
                String lonStr = String.valueOf(location.longitude);
                String latStr = String.valueOf(location.latitude);
                response.append("$").append(lonStr.length()).append("\r\n").append(lonStr).append("\r\n");
                response.append("$").append(latStr.length()).append("\r\n").append(latStr).append("\r\n");
            } else {
                response.append("$-1\r\n");
            }
        }
        
        return response.toString();
    }
    
    /**
     * 处理 GEODIST 命令
     * 格式：GEODIST key member1 member2 [unit]
     */
    private String handleGeoDist(List<String> args) {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'geodist' command\r\n";
        }
        
        String key = args.get(0);
        String member1 = args.get(1);
        String member2 = args.get(2);
        String unit = args.size() > 3 ? args.get(3).toLowerCase() : "m";
        
        Map<String, GeoLocation> locations = geoData.get(key);
        if (locations == null) {
            return "$-1\r\n";
        }
        
        GeoLocation loc1 = locations.get(member1);
        GeoLocation loc2 = locations.get(member2);
        
        if (loc1 == null || loc2 == null) {
            return "$-1\r\n";
        }
        
        // 计算距离（使用简化的球面距离公式）
        double distance = calculateDistance(loc1, loc2, unit);
        String distStr = String.valueOf(distance);
        
        return "$" + distStr.length() + "\r\n" + distStr + "\r\n";
    }
    
    /**
     * 处理 GEORADIUS 命令
     * 格式：GEORADIUS key longitude latitude radius unit [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count] [ASC|DESC]
     */
    private String handleGeoRadius(List<String> args) {
        if (args.size() < 4) {
            return "-ERR wrong number of arguments for 'georadius' command\r\n";
        }
        
        try {
            String key = args.get(0);
            double longitude = Double.parseDouble(args.get(1));
            double latitude = Double.parseDouble(args.get(2));
            double radius = Double.parseDouble(args.get(3));
            String unit = args.size() > 4 ? args.get(4).toLowerCase() : "m";
            
            // 模拟返回范围内的成员
            return "*1\r\n$7\r\nmember1\r\n";
        } catch (NumberFormatException e) {
            return "-ERR value is not a valid float\r\n";
        }
    }
    
    /**
     * 处理 GEORADIUSBYMEMBER 命令
     * 格式：GEORADIUSBYMEMBER key member radius unit [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count] [ASC|DESC]
     */
    private String handleGeoRadiusByMember(List<String> args) {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'georadiusbymember' command\r\n";
        }
        
        String key = args.get(0);
        String member = args.get(1);
        
        try {
            double radius = Double.parseDouble(args.get(2));
            String unit = args.size() > 3 ? args.get(3).toLowerCase() : "m";
            
            Map<String, GeoLocation> locations = geoData.get(key);
            if (locations == null || !locations.containsKey(member)) {
                return "*0\r\n";
            }
            
            // 模拟返回范围内的成员
            return "*1\r\n$7\r\nmember1\r\n";
        } catch (NumberFormatException e) {
            return "-ERR value is not a valid float\r\n";
        }
    }
    
    /**
     * 处理 GEOHASH 命令
     * 格式：GEOHASH key member [member ...]
     */
    private String handleGeoHash(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'geohash' command\r\n";
        }
        
        String key = args.get(0);
        Map<String, GeoLocation> locations = geoData.get(key);
        
        StringBuilder response = new StringBuilder();
        response.append("*").append(args.size() - 1).append("\r\n");
        
        for (int i = 1; i < args.size(); i++) {
            String member = args.get(i);
            GeoLocation location = locations != null ? locations.get(member) : null;
            
            if (location != null) {
                // 生成模拟的 geohash
                String geohash = generateGeoHash(location.longitude, location.latitude);
                response.append("$").append(geohash.length()).append("\r\n").append(geohash).append("\r\n");
            } else {
                response.append("$-1\r\n");
            }
        }
        
        return response.toString();
    }
    
    /**
     * 计算两点间距离
     */
    private double calculateDistance(GeoLocation loc1, GeoLocation loc2, String unit) {
        double lat1Rad = Math.toRadians(loc1.latitude);
        double lat2Rad = Math.toRadians(loc2.latitude);
        double deltaLatRad = Math.toRadians(loc2.latitude - loc1.latitude);
        double deltaLonRad = Math.toRadians(loc2.longitude - loc1.longitude);
        
        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        double distance = 6371000 * c; // 地球半径（米）
        
        // 转换单位
        switch (unit) {
            case "km":
                return distance / 1000;
            case "mi":
                return distance / 1609.344;
            case "ft":
                return distance * 3.28084;
            default: // "m"
                return distance;
        }
    }
    
    /**
     * 生成模拟的 GeoHash
     */
    private String generateGeoHash(double longitude, double latitude) {
        // 简化的 geohash 生成（实际应该使用专门的算法）
        return String.format("wx4g%08x", (int)(longitude * latitude * 1000000));
    }
    
    /**
     * 地理位置数据类
     */
    private static class GeoLocation {
        final double longitude;
        final double latitude;
        
        GeoLocation(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }
}