// Generated from com/sqladaptor/grammar/Redis.g4 by ANTLR 4.13.1
package com.sqladaptor.grammar;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RedisParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RedisVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RedisParser#command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommand(RedisParser.CommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#commandName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommandName(RedisParser.CommandNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#stringCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringCommand(RedisParser.StringCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#hashCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHashCommand(RedisParser.HashCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#listCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListCommand(RedisParser.ListCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#setCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetCommand(RedisParser.SetCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#sortedSetCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortedSetCommand(RedisParser.SortedSetCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#keyCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyCommand(RedisParser.KeyCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#connectionCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectionCommand(RedisParser.ConnectionCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#serverCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitServerCommand(RedisParser.ServerCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#pubsubCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPubsubCommand(RedisParser.PubsubCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#transactionCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionCommand(RedisParser.TransactionCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#scriptCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScriptCommand(RedisParser.ScriptCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#geoCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeoCommand(RedisParser.GeoCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(RedisParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#numberLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberLiteral(RedisParser.NumberLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(RedisParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#commandKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommandKeyword(RedisParser.CommandKeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(RedisParser.ArgumentContext ctx);
}