package com.sqladaptor.protocol;

import java.util.List;

public class RedisCommand {
    private final String command;
    private final List<String> args;
    
    public RedisCommand(String command, List<String> args) {
        this.command = command;
        this.args = args;
    }
    
    public String getCommand() {
        return command;
    }
    
    public List<String> getArgs() {
        return args;
    }
    
    @Override
    public String toString() {
        return "RedisCommand{" +
                "command='" + command + '\'' +
                ", args=" + args +
                '}';
    }
}