package com.sqladaptor.protocol.handlers;

import com.sqladaptor.ast.RedisCommandNode;
import com.sqladaptor.database.DatabaseManager;
import com.sqladaptor.converter.RedisToSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCommandHandler {
    protected static final Logger logger = LoggerFactory.getLogger(BaseCommandHandler.class);
    protected final DatabaseManager databaseManager;
    protected final RedisToSqlConverter converter;
    
    public BaseCommandHandler(DatabaseManager databaseManager, RedisToSqlConverter converter) {
        this.databaseManager = databaseManager;
        this.converter = converter;
    }
    
    public abstract String handle(RedisCommandNode commandNode) throws Exception;
}