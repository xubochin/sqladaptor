package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServerCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerCommandHandler.class);
    
    private final DatabaseManager databaseManager;
    private final RedisToSqlConverter converter;
    private final Map<String, String> configSettings = new HashMap<>();
    private static final AtomicInteger activeConnections = new AtomicInteger(0);
    private static final AtomicLong totalConnections = new AtomicLong(0);
    
    public ServerCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        this.databaseManager = databaseManager;
        this.converter = converter;
        initializeDefaultConfig();
    }
    
    private void initializeDefaultConfig() {
        configSettings.put("timeout", "0");
        configSettings.put("databases", "16");
        configSettings.put("maxmemory", "0");
        configSettings.put("maxmemory-policy", "noeviction");
    }
    
    public String handle(RedisCommandNode commandNode) throws Exception {
        String command = commandNode.getCommand().toUpperCase();
        List<String> args = commandNode.getArguments();
        
        logger.debug("Handling SERVER command: {} with args: {}", command, args);
        
        switch (command) {
            case "SELECT":
                return handleSelect(args);
            case "CONFIG":
                return handleConfig(args);
            case "INFO":
                return handleInfo(args);
            case "FLUSHDB":
                return handleFlushDb(args);
            case "FLUSHALL":
                return handleFlushAll(args);
            case "DBSIZE":
                return handleDbSize(args);
            case "BGREWRITEAOF":
                return handleBgRewriteAof(args);
            case "BGSAVE":
                return handleBgSave(args);
            case "TIME":
                return handleTime(args);
            case "LASTSAVE":
                return handleLastSave(args);
            case "SAVE":
                return handleSave(args);
            case "SHUTDOWN":
                return handleShutdown(args);
            case "SLAVEOF":
                return handleSlaveOf(args);
            case "ROLE":
                return handleRole(args);
            case "MONITOR":
                return handleMonitor(args);
            case "SLOWLOG":
                return handleSlowLog(args);
            case "SYNC":
                return handleSync(args);
            case "COMMAND":
                return handleCommand(args);
            case "DEBUG":
                return handleDebug(args);
            default:
                return "-ERR Unknown SERVER command '" + command + "'\r\n";
        }
    }
    
    private String handleSelect(List<String> args) {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'select' command\r\n";
        }
        
        try {
            int dbIndex = Integer.parseInt(args.get(0));
            if (dbIndex < 0 || dbIndex >= 16) {
                return "-ERR DB index is out of range\r\n";
            }
            return "+OK\r\n";
        } catch (NumberFormatException e) {
            return "-ERR invalid DB index\r\n";
        }
    }
    
    private String handleConfig(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'config' command\r\n";
        }
        
        String subCommand = args.get(0).toUpperCase();
        switch (subCommand) {
            case "GET":
                return handleConfigGet(args.subList(1, args.size()));
            case "SET":
                return handleConfigSet(args.subList(1, args.size()));
            case "REWRITE":
                return handleConfigRewrite(args.subList(1, args.size()));
            case "RESETSTAT":
                return handleConfigResetStat(args.subList(1, args.size()));
            default:
                return "-ERR Unknown CONFIG subcommand '" + subCommand + "'\r\n";
        }
    }
    
    private String handleConfigGet(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'config get' command\r\n";
        }
        
        String pattern = args.get(0);
        List<String> result = new ArrayList<>();
        
        for (Map.Entry<String, String> entry : configSettings.entrySet()) {
            if (entry.getKey().matches(pattern.replace("*", ".*"))) {
                result.add(entry.getKey());
                result.add(entry.getValue());
            }
        }
        
        StringBuilder response = new StringBuilder();
        response.append("*").append(result.size()).append("\r\n");
        for (String item : result) {
            response.append("$").append(item.length()).append("\r\n");
            response.append(item).append("\r\n");
        }
        
        return response.toString();
    }
    
    private String handleConfigSet(List<String> args) {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'config set' command\r\n";
        }
        
        String key = args.get(0);
        String value = args.get(1);
        
        configSettings.put(key, value);
        return "+OK\r\n";
    }
    
    private String handleConfigRewrite(List<String> args) {
        return "+OK\r\n";
    }
    
    private String handleConfigResetStat(List<String> args) {
        return "+OK\r\n";
    }
    
    private String handleInfo(List<String> args) {
        String section = args.isEmpty() ? "default" : args.get(0).toLowerCase();
        
        StringBuilder info = new StringBuilder();
        
        switch (section) {
            case "server":
            case "default":
                info.append("# Server\r\n");
                info.append("redis_version:7.0.0\r\n");
                info.append("redis_git_sha1:00000000\r\n");
                info.append("redis_git_dirty:0\r\n");
                info.append("redis_build_id:0\r\n");
                info.append("redis_mode:standalone\r\n");
                info.append("os:Windows\r\n");
                info.append("arch_bits:64\r\n");
                info.append("multiplexing_api:select\r\n");
                info.append("atomicvar_api:atomic-builtin\r\n");
                info.append("gcc_version:0.0.0\r\n");
                info.append("process_id:1\r\n");
                info.append("run_id:0\r\n");
                info.append("tcp_port:6379\r\n");
                info.append("uptime_in_seconds:1\r\n");
                info.append("uptime_in_days:0\r\n");
                info.append("hz:10\r\n");
                info.append("configured_hz:10\r\n");
                info.append("lru_clock:0\r\n");
                info.append("executable:/usr/local/bin/redis-server\r\n");
                info.append("config_file:\r\n");
                if (!section.equals("server")) {
                    info.append("\r\n");
                }
                break;
            case "clients":
                info.append("# Clients\r\n");
                info.append("connected_clients:").append(activeConnections.get()).append("\r\n");
                info.append("client_recent_max_input_buffer:0\r\n");
                info.append("client_recent_max_output_buffer:0\r\n");
                info.append("blocked_clients:0\r\n");
                info.append("tracking_clients:0\r\n");
                info.append("clients_in_timeout_table:0\r\n");
                break;
            case "memory":
                info.append("# Memory\r\n");
                info.append("used_memory:1024\r\n");
                info.append("used_memory_human:1.00K\r\n");
                info.append("used_memory_rss:1024\r\n");
                info.append("used_memory_rss_human:1.00K\r\n");
                info.append("used_memory_peak:1024\r\n");
                info.append("used_memory_peak_human:1.00K\r\n");
                info.append("used_memory_peak_perc:100.00%\r\n");
                info.append("used_memory_overhead:0\r\n");
                info.append("used_memory_startup:0\r\n");
                info.append("used_memory_dataset:1024\r\n");
                info.append("used_memory_dataset_perc:100.00%\r\n");
                info.append("allocator_allocated:0\r\n");
                info.append("allocator_active:0\r\n");
                info.append("allocator_resident:0\r\n");
                info.append("total_system_memory:0\r\n");
                info.append("total_system_memory_human:0B\r\n");
                info.append("used_memory_lua:0\r\n");
                info.append("used_memory_lua_human:0B\r\n");
                info.append("used_memory_scripts:0\r\n");
                info.append("used_memory_scripts_human:0B\r\n");
                info.append("number_of_cached_scripts:0\r\n");
                info.append("maxmemory:0\r\n");
                info.append("maxmemory_human:0B\r\n");
                info.append("maxmemory_policy:noeviction\r\n");
                info.append("allocator_frag_ratio:0.00\r\n");
                info.append("allocator_frag_bytes:0\r\n");
                info.append("allocator_rss_ratio:0.00\r\n");
                info.append("allocator_rss_bytes:0\r\n");
                info.append("rss_overhead_ratio:0.00\r\n");
                info.append("rss_overhead_bytes:0\r\n");
                info.append("mem_fragmentation_ratio:0.00\r\n");
                info.append("mem_fragmentation_bytes:0\r\n");
                info.append("mem_not_counted_for_evict:0\r\n");
                info.append("mem_replication_backlog:0\r\n");
                info.append("mem_clients_slaves:0\r\n");
                info.append("mem_clients_normal:0\r\n");
                info.append("mem_aof_buffer:0\r\n");
                info.append("mem_allocator:libc\r\n");
                info.append("active_defrag_running:0\r\n");
                info.append("lazyfree_pending_objects:0\r\n");
                break;
            case "persistence":
                info.append("# Persistence\r\n");
                info.append("loading:0\r\n");
                info.append("rdb_changes_since_last_save:0\r\n");
                info.append("rdb_bgsave_in_progress:0\r\n");
                info.append("rdb_last_save_time:0\r\n");
                info.append("rdb_last_bgsave_status:ok\r\n");
                info.append("rdb_last_bgsave_time_sec:0\r\n");
                info.append("rdb_current_bgsave_time_sec:-1\r\n");
                info.append("rdb_last_cow_size:0\r\n");
                info.append("aof_enabled:0\r\n");
                info.append("aof_rewrite_in_progress:0\r\n");
                info.append("aof_rewrite_scheduled:0\r\n");
                info.append("aof_last_rewrite_time_sec:-1\r\n");
                info.append("aof_current_rewrite_time_sec:-1\r\n");
                info.append("aof_last_bgrewrite_status:ok\r\n");
                info.append("aof_last_write_status:ok\r\n");
                info.append("aof_last_cow_size:0\r\n");
                break;
            case "stats":
                info.append("# Stats\r\n");
                info.append("total_connections_received:").append(totalConnections.get()).append("\r\n");
                info.append("total_commands_processed:0\r\n");
                info.append("instantaneous_ops_per_sec:0\r\n");
                info.append("total_net_input_bytes:0\r\n");
                info.append("total_net_output_bytes:0\r\n");
                info.append("instantaneous_input_kbps:0.00\r\n");
                info.append("instantaneous_output_kbps:0.00\r\n");
                info.append("rejected_connections:0\r\n");
                info.append("sync_full:0\r\n");
                info.append("sync_partial_ok:0\r\n");
                info.append("sync_partial_err:0\r\n");
                info.append("expired_keys:0\r\n");
                info.append("expired_stale_perc:0.00\r\n");
                info.append("expired_time_cap_reached_count:0\r\n");
                info.append("expire_cycle_cpu_milliseconds:0\r\n");
                info.append("evicted_keys:0\r\n");
                info.append("keyspace_hits:0\r\n");
                info.append("keyspace_misses:0\r\n");
                info.append("pubsub_channels:0\r\n");
                info.append("pubsub_patterns:0\r\n");
                info.append("latest_fork_usec:0\r\n");
                info.append("migrate_cached_sockets:0\r\n");
                info.append("slave_expires_tracked_keys:0\r\n");
                info.append("active_defrag_hits:0\r\n");
                info.append("active_defrag_misses:0\r\n");
                info.append("active_defrag_key_hits:0\r\n");
                info.append("active_defrag_key_misses:0\r\n");
                break;
            case "replication":
                info.append("# Replication\r\n");
                info.append("role:master\r\n");
                info.append("connected_slaves:0\r\n");
                info.append("master_replid:0000000000000000000000000000000000000000\r\n");
                info.append("master_replid2:0000000000000000000000000000000000000000\r\n");
                info.append("master_repl_offset:0\r\n");
                info.append("second_repl_offset:-1\r\n");
                info.append("repl_backlog_active:0\r\n");
                info.append("repl_backlog_size:1048576\r\n");
                info.append("repl_backlog_first_byte_offset:0\r\n");
                info.append("repl_backlog_histlen:0\r\n");
                break;
            case "cpu":
                info.append("# CPU\r\n");
                info.append("used_cpu_sys:0.00\r\n");
                info.append("used_cpu_user:0.00\r\n");
                info.append("used_cpu_sys_children:0.00\r\n");
                info.append("used_cpu_user_children:0.00\r\n");
                break;
            case "commandstats":
                info.append("# Commandstats\r\n");
                break;
            case "cluster":
                info.append("# Cluster\r\n");
                info.append("cluster_enabled:0\r\n");
                break;
            case "keyspace":
                info.append("# Keyspace\r\n");
                break;
            default:
                return "-ERR Invalid INFO section\r\n";
        }
        
        String result = info.toString();
        return "$" + result.length() + "\r\n" + result + "\r\n";
    }
    
    private String handleFlushDb(List<String> args) {
        try {
            // 模拟清空当前数据库
            return "+OK\r\n";
        } catch (Exception e) {
            logger.error("Error executing FLUSHDB", e);
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }
    
    private String handleFlushAll(List<String> args) {
        try {
            // 模拟清空所有数据库
            return "+OK\r\n";
        } catch (Exception e) {
            logger.error("Error executing FLUSHALL", e);
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }
    
    private String handleDbSize(List<String> args) {
        try {
            // 模拟返回数据库大小
            return ":0\r\n";
        } catch (Exception e) {
            logger.error("Error executing DBSIZE", e);
            return "-ERR " + e.getMessage() + "\r\n";
        }
    }
    
    private String handleBgRewriteAof(List<String> args) {
        return "+Background append only file rewriting started\r\n";
    }
    
    private String handleBgSave(List<String> args) {
        return "+Background saving started\r\n";
    }
    
    private String handleTime(List<String> args) {
        long currentTime = System.currentTimeMillis();
        long seconds = currentTime / 1000;
        long microseconds = (currentTime % 1000) * 1000;
        
        return "*2\r\n$" + String.valueOf(seconds).length() + "\r\n" + seconds + "\r\n$" + 
               String.valueOf(microseconds).length() + "\r\n" + microseconds + "\r\n";
    }
    
    private String handleLastSave(List<String> args) {
        long lastSave = System.currentTimeMillis() / 1000;
        return ":" + lastSave + "\r\n";
    }
    
    private String handleSave(List<String> args) {
        return "+OK\r\n";
    }
    
    private String handleShutdown(List<String> args) {
        return "+OK\r\n";
    }
    
    private String handleSlaveOf(List<String> args) {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'slaveof' command\r\n";
        }
        return "+OK\r\n";
    }
    
    private String handleRole(List<String> args) {
        return "*3\r\n$6\r\nmaster\r\n:0\r\n*0\r\n";
    }
    
    private String handleMonitor(List<String> args) {
        return "+OK\r\n";
    }
    
    private String handleSlowLog(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'slowlog' command\r\n";
        }
        
        String subCommand = args.get(0).toUpperCase();
        switch (subCommand) {
            case "GET":
                return "*0\r\n"; // 返回空列表
            case "LEN":
                return ":0\r\n";
            case "RESET":
                return "+OK\r\n";
            default:
                return "-ERR Unknown SLOWLOG subcommand '" + subCommand + "'\r\n";
        }
    }
    
    private String handleSync(List<String> args) {
        return "+FULLRESYNC 0000000000000000000000000000000000000000 0\r\n";
    }
    
    private String handleCommand(List<String> args) {
        if (args.isEmpty()) {
            return "*0\r\n"; // 返回空列表
        }
        
        String subCommand = args.get(0).toUpperCase();
        switch (subCommand) {
            case "COUNT":
                return ":0\r\n";
            case "GETKEYS":
                return "*0\r\n";
            case "INFO":
                return "*0\r\n";
            default:
                return "-ERR Unknown COMMAND subcommand '" + subCommand + "'\r\n";
        }
    }
    
    private String handleDebug(List<String> args) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'debug' command\r\n";
        }
        
        String subCommand = args.get(0).toUpperCase();
        switch (subCommand) {
            case "OBJECT":
                return "+OK\r\n";
            case "SEGFAULT":
                return "+OK\r\n";
            default:
                return "-ERR Unknown DEBUG subcommand '" + subCommand + "'\r\n";
        }
    }
    
    public void incrementActiveConnections() {
        activeConnections.incrementAndGet();
        totalConnections.incrementAndGet();
    }
    
    public void decrementActiveConnections() {
        activeConnections.decrementAndGet();
    }
}