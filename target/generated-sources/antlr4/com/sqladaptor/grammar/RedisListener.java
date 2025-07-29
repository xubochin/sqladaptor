// Generated from com/sqladaptor/grammar/Redis.g4 by ANTLR 4.13.1
package com.sqladaptor.grammar;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RedisParser}.
 */
public interface RedisListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RedisParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCommand(RedisParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCommand(RedisParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#commandName}.
	 * @param ctx the parse tree
	 */
	void enterCommandName(RedisParser.CommandNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#commandName}.
	 * @param ctx the parse tree
	 */
	void exitCommandName(RedisParser.CommandNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(RedisParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(RedisParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#numberLiteral}.
	 * @param ctx the parse tree
	 */
	void enterNumberLiteral(RedisParser.NumberLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#numberLiteral}.
	 * @param ctx the parse tree
	 */
	void exitNumberLiteral(RedisParser.NumberLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(RedisParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(RedisParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(RedisParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(RedisParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#commandKeyword}.
	 * @param ctx the parse tree
	 */
	void enterCommandKeyword(RedisParser.CommandKeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#commandKeyword}.
	 * @param ctx the parse tree
	 */
	void exitCommandKeyword(RedisParser.CommandKeywordContext ctx);
}