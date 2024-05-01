package com.compiler.lexicalanalyzer.Parser;

import java.util.HashMap;
import java.util.Map;

public class PredictiveTable {
    private Map<String, Map<String, Production>> table = new HashMap<>();

    public PredictiveTable(Grammar grammar) {
        generateTable(grammar);
    }

    private void generateTable(Grammar grammar) {
        var firstFollow = new FirstFollow(grammar);
        for (var nonTerminal : grammar.getNonTerminals()) {
            for (var production : grammar.getProductions(nonTerminal)) {
                for (var first : firstFollow.firstSet(production)) {
                    if (first.equals(grammar.getEmptySymbol())) {
                        var epsilon = new Production(grammar.getEmptySymbol());
                        for (var follow : firstFollow.followSet(nonTerminal)) {
                            var cellContent = follow.equals("$") ? production : epsilon;
                            if (table.containsKey(follow))
                                table.get(follow).put(nonTerminal, cellContent);
                            else {
                                Map<String, Production> column = new HashMap<>();
                                column.put(nonTerminal, cellContent);
                                table.put(follow, column);
                            }
                        }
                    }
                    else {
                        if (table.containsKey(first))
                            table.get(first).put(nonTerminal, production);
                        else {
                            Map<String, Production> column = new HashMap<>();
                            column.put(nonTerminal, production);
                            table.put(first, column);
                        }
                    }
                }
            }
        }
    }

    public Map<String, Map<String, Production>> getTable() {
        System.out.println(table.get("if"));
        return table;
    }
}
