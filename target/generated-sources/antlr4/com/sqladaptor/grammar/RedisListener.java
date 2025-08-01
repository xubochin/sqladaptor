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
	 * Enter a parse tree produced by {@link RedisParser#stringCommand}.
	 * @param ctx the parse tree
	 */
	void enterStringCommand(RedisParser.StringCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#stringCommand}.
	 * @param ctx the parse tree
	 */
	void exitStringCommand(RedisParser.StringCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#hashCommand}.
	 * @param ctx the parse tree
	 */
	void enterHashCommand(RedisParser.HashCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#hashCommand}.
	 * @param ctx the parse tree
	 */
	void exitHashCommand(RedisParser.HashCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#listCommand}.
	 * @param ctx the parse tree
	 */
	void enterListCommand(RedisParser.ListCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#listCommand}.
	 * @param ctx the parse tree
	 */
	void exitListCommand(RedisParser.ListCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#setCommand}.
	 * @param ctx the parse tree
	 */
	void enterSetCommand(RedisParser.SetCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#setCommand}.
	 * @param ctx the parse tree
	 */
	void exitSetCommand(RedisParser.SetCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#sortedSetCommand}.
	 * @param ctx the parse tree
	 */
	void enterSortedSetCommand(RedisParser.SortedSetCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#sortedSetCommand}.
	 * @param ctx the parse tree
	 */
	void exitSortedSetCommand(RedisParser.SortedSetCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#keyCommand}.
	 * @param ctx the parse tree
	 */
	void enterKeyCommand(RedisParser.KeyCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#keyCommand}.
	 * @param ctx the parse tree
	 */
	void exitKeyCommand(RedisParser.KeyCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#connectionCommand}.
	 * @param ctx the parse tree
	 */
	void enterConnectionCommand(RedisParser.ConnectionCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#connectionCommand}.
	 * @param ctx the parse tree
	 */
	void exitConnectionCommand(RedisParser.ConnectionCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#serverCommand}.
	 * @param ctx the parse tree
	 */
	void enterServerCommand(RedisParser.ServerCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#serverCommand}.
	 * @param ctx the parse tree
	 */
	void exitServerCommand(RedisParser.ServerCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#pubsubCommand}.
	 * @param ctx the parse tree
	 */
	void enterPubsubCommand(RedisParser.PubsubCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#pubsubCommand}.
	 * @param ctx the parse tree
	 */
	void exitPubsubCommand(RedisParser.PubsubCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#transactionCommand}.
	 * @param ctx the parse tree
	 */
	void enterTransactionCommand(RedisParser.TransactionCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#transactionCommand}.
	 * @param ctx the parse tree
	 */
	void exitTransactionCommand(RedisParser.TransactionCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#scriptCommand}.
	 * @param ctx the parse tree
	 */
	void enterScriptCommand(RedisParser.ScriptCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#scriptCommand}.
	 * @param ctx the parse tree
	 */
	void exitScriptCommand(RedisParser.ScriptCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RedisParser#geoCommand}.
	 * @param ctx the parse tree
	 */
	void enterGeoCommand(RedisParser.GeoCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#geoCommand}.
	 * @param ctx the parse tree
	 */
	void exitGeoCommand(RedisParser.GeoCommandContext ctx);
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
	 * Enter a parse tree produced by {@link RedisParser#commandKeyword}.
	 * @param ctx the parse tree
	 */
	void enterCommandKeyword(RedisParser.CommandKeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link RedisParser#commandKeyword}.
	 * @param ctx the parse tree
	 */
	void exitCommandKeyword(RedisParser.CommandKeywordContext ctx);
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
}