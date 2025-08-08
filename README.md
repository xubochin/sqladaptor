# Redis to JDBC Adapter

一个将Redis协议转换为JDBC数据库访问的适配器服务，支持SQLite和VoltDB。采用ANTLR4进行Redis命令解析，提供高性能和可扩展的架构。

## 功能特性

- 🚀 基于ANTLR4的强大Redis命令解析器
- 📊 支持Redis基本命令：SET, GET, DEL, HSET, HGET, HDEL, HGETALL, INCR, DECR, PING, ECHO
- 🗄️ 支持SQLite和VoltDB数据库
- ⚡ 基于Netty的高性能网络处理
- 🔧 完整的Redis协议解析和AST构建
- ⚙️ 可配置的数据库连接
- 🛡️ 类型安全的命令处理
- 📈 易于扩展新的Redis命令
- 🆕 **新增高级命令支持**：
  - 数据库选择：SELECT
  - 配置管理：CONFIG GET/SET
  - HyperLogLog：PFADD, PFCOUNT, PFMERGE
  - 地理位置：GEOADD, GEOPOS, GEODIST, GEORADIUS, GEORADIUSBYMEMBER
  - 流处理：XADD, XRANGE, XREAD, XGROUP, XREADGROUP
  - 位字段：BITFIELD
  - Lua脚本：SCRIPT LOAD, EVAL, EVALSHA（有限支持）
- 🌐 **多语言支持**：
  - 中文 (zh_CN)
  - 英文 (en_US) 
  - 日文 (ja_JP)
  - 可配置的语言环境设置

## 支持的Redis命令

### 字符串操作
- `SET key value` - 设置键值对
- `GET key` - 获取键的值
- `DEL key [key ...]` - 删除一个或多个键
- `INCR key` - 将键的值递增1
- `DECR key` - 将键的值递减1

### 哈希操作
- `HSET key field value` - 设置哈希字段的值
- `HGET key field` - 获取哈希字段的值
- `HDEL key field [field ...]` - 删除哈希字段
- `HGETALL key` - 获取哈希的所有字段和值

### 数据库操作
- `SELECT index` - 选择数据库（0-15）

### 配置管理
- `CONFIG GET pattern` - 获取配置参数
- `CONFIG SET parameter value` - 设置配置参数（模拟，不实际生效）

### HyperLogLog操作
- `PFADD key element [element ...]` - 添加元素到HyperLogLog
- `PFCOUNT key [key ...]` - 返回HyperLogLog的基数估计
- `PFMERGE destkey sourcekey [sourcekey ...]` - 合并HyperLogLog

### 地理位置操作
- `GEOADD key longitude latitude member [longitude latitude member ...]` - 添加地理位置
- `GEOPOS key member [member ...]` - 获取地理位置坐标
- `GEODIST key member1 member2 [unit]` - 计算两点间距离
- `GEORADIUS key longitude latitude radius unit` - 查询范围内的成员
- `GEORADIUSBYMEMBER key member radius unit` - 以成员为中心查询范围

### 流操作
- `XADD key ID field value [field value ...]` - 添加流条目
- `XRANGE key start end` - 获取流范围内的条目
- `XREAD [COUNT count] [BLOCK milliseconds] STREAMS key [key ...] id [id ...]` - 读取流
- `XGROUP [CREATE key groupname id] [DESTROY key groupname]` - 管理消费者组
- `XREADGROUP GROUP group consumer [COUNT count] STREAMS key [key ...] ID [ID ...]` - 组读取

### 位字段操作
- `BITFIELD key [GET type offset] [SET type offset value] [INCRBY type offset increment]` - 位字段操作

### Lua脚本
- `SCRIPT LOAD script` - 加载Lua脚本
- `SCRIPT EXISTS sha1 [sha1 ...]` - 检查脚本是否存在
- `SCRIPT FLUSH` - 清空脚本缓存
- `EVAL script numkeys key [key ...] arg [arg ...]` - 执行Lua脚本（返回错误）
- `EVALSHA sha1 numkeys key [key ...] arg [arg ...]` - 执行已加载的脚本（返回错误）

### 连接测试
- `PING [message]` - 测试连接
- `ECHO message` - 回显消息

## 注意事项

### 命令实现说明

1. **SELECT命令**：支持数据库选择（0-15），但实际上所有数据库共享同一个物理存储
2. **CONFIG命令**：GET命令返回模拟的配置值，SET命令接受设置但不实际生效
3. **HyperLogLog命令**：提供模拟实现，返回固定的估计值
4. **地理位置命令**：提供模拟实现，返回示例坐标和距离
5. **流命令**：提供基础的模拟实现，支持基本的流操作语法
6. **位字段命令**：提供模拟实现，返回示例操作结果
7. **Lua脚本**：支持脚本加载和管理，但执行时返回"不支持"错误

### 限制说明

- ⚠️ 当前实现专注于Redis命令的语法兼容性，部分高级功能为模拟实现
- 🔄 事务支持有限，建议在应用层处理事务逻辑
- 📊 性能可通过连接池、批量操作等方式进一步提升
- 🔒 生产环境使用时请考虑安全性配置
- 📈 监控和日志记录可根据需要进行扩展
- 🚫 Lua脚本执行功能受限，仅支持脚本管理操作
- 🌐 多语言环境需要重启服务生效

## 项目目录结构

```
sql-adptor-lite/
├── README.md                    # 项目说明文档
├── pom.xml                      # Maven 配置文件
├── redis_adapter.db             # SQLite 数据库文件
├── src/
│   ├── main/
│   │   ├── antlr4/              # ANTLR4 语法文件
│   │   │   └── com/sqladaptor/grammar/
│   │   │       └── Redis.g4     # Redis 命令语法定义
│   │   ├── java/com/sqladaptor/
│   │   │   ├── RedisToJdbcServer.java      # 主服务器类
│   │   │   ├── ast/             # 抽象语法树相关
│   │   │   ├── config/          # 配置管理
│   │   │   ├── converter/       # Redis 到 SQL 转换器
│   │   │   ├── database/        # 数据库管理
│   │   │   ├── parser/          # Redis 命令解析器
│   │   │   └── protocol/        # Redis 协议处理
│   │   │       └── handlers/    # 各类命令处理器
│   │   └── resources/
│   │       ├── application.properties       # 应用配置
│   │       ├── messages.properties          # 英文消息
│   │       └── messages_zh_CN.properties    # 中文消息
│   └── test/                    # 完整的单元测试和集成测试
└── target/                      # Maven 编译输出目录
```

## 技术栈

- **Java 11+**: 核心开发语言
- **ANTLR4**: 语法解析器生成器
- **Netty**: 高性能网络框架
- **SQLite/VoltDB**: 支持的数据库
- **SLF4J + Logback**: 日志框架
- **Jackson**: JSON处理
- **Maven**: 构建工具

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request来改进项目！

## 快速开始

### 编译项目

```bash
mvn clean compile
```

### 运行服务

```bash
mvn exec:java -Dexec.mainClass="com.sqladaptor.RedisToJdbcServer"
```

### 测试连接

使用Redis客户端连接到localhost:6379：

```bash
redis-cli -h localhost -p 6379
```

## 支持的Redis命令

- `SET key value` - 设置键值对
- `GET key` - 获取键的值
- `DEL key [key ...]` - 删除一个或多个键
- `HSET key field value` - 设置哈希字段的值
- `HGET key field` - 获取哈希字段的值
- `PING` - 测试连接

## 配置

### 数据库配置

编辑 `src/main/resources/application.properties` 文件来配置数据库连接：

```properties
# 使用SQLite
database.type=sqlite
database.path=redis_adapter.db

# 使用VoltDB
# database.type=voltdb
# database.host=localhost
# database.port=21212
# database.username=your_username
# database.password=your_password
```

### 语言配置

配置应用程序的语言环境：

```properties
# 语言和地区配置 / Language and Locale Configuration
application.locale=zh_CN          # 当前语言 (zh_CN, en_US, ja_JP)
application.default.locale=en_US  # 默认语言
application.supported.locales=en_US,zh_CN,ja_JP  # 支持的语言列表
```

### 日志配置

精细化控制日志级别：

```properties
# 日志配置
logging.level.root=INFO
logging.level.com.sqladaptor=INFO
logging.level.com.sqladaptor.protocol.RedisProtocolHandler=DEBUG
logging.level.com.sqladaptor.database.DatabaseManager=INFO
```

## 数据库表结构

### redis_kv 表（键值对）
- key_name (VARCHAR): Redis键名
- value_data (TEXT): 值数据
- created_at (TIMESTAMP): 创建时间
- updated_at (TIMESTAMP): 更新时间

### redis_hash 表（哈希）
- key_name (VARCHAR): Redis键名
- field_name (VARCHAR): 字段名
- value_data (TEXT): 值数据
- created_at (TIMESTAMP): 创建时间
- updated_at (TIMESTAMP): 更新时间

## 架构说明

1. **RedisProtocolHandler**: 处理Redis协议解析和命令分发
2. **DatabaseManager**: 管理数据库连接和SQL执行
3. **RedisToSqlConverter**: 将Redis命令转换为SQL语句
4. **DatabaseConfig**: 处理数据库配置

## 扩展支持

要添加新的Redis命令支持：

1. 在 `RedisProtocolHandler` 中添加新的命令处理方法
2. 在 `RedisToSqlConverter` 中添加相应的SQL转换方法
3. 如需要，更新数据库表结构

## 注意事项

- 当前实现是基础版本，不支持Redis的所有高级特性
- 事务支持有限
- 性能优化可以通过连接池等方式进一步提升