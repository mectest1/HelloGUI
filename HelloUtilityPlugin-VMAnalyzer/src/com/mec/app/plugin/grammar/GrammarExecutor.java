package com.mec.app.plugin.grammar;

import com.mec.app.plugin.grammar.Grammar.ParseResult;

public interface GrammarExecutor<T, R> {

	R execute(ParseResult<T> parseResult);
}
