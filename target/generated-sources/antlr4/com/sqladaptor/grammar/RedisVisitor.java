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
	 * Visit a parse tree produced by {@link RedisParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(RedisParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link RedisParser#commandKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommandKeyword(RedisParser.CommandKeywordContext ctx);
}