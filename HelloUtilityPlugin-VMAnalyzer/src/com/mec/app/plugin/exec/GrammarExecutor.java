package com.mec.app.plugin.exec;

import com.mec.app.plugin.grammar.Grammar.ParseResult;

public interface GrammarExecutor {

	<T> void execute(ParseResult<T> parseResult);
}
