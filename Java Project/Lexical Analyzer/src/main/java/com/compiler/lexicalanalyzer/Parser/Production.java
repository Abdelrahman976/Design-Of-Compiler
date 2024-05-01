package com.compiler.lexicalanalyzer.Parser;

import java.util.ArrayList;
import java.util.List;

public class Production {
    private List<String> symbols = new ArrayList<>();

    public Production(String... symbols) {
        for (var symbol : symbols)
            this.symbols.add(symbol);
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public int length() {
        return symbols.size();
    }
}
