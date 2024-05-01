package com.compiler.lexicalanalyzer.Parser;

import com.compiler.lexicalanalyzer.LexicalAnalyzer;

import java.io.File;
import java.util.*;

public class AnalysisTable {
    private File grammarFile;
    private File sourceFile;
    private Grammar grammar;
    private String source;
    private String[] columnNames;
    private Deque<String[]> table;
    private boolean complete;

    public AnalysisTable(File grammarFile, File sourceFile) {
        this.grammarFile = grammarFile;
        this.sourceFile = sourceFile;
        grammar = new Grammar(grammarFile);
        source = PreProcessor.getStrippedSource(sourceFile);
        columnNames = new String[]{"Stack", "Tokens Queue", "Decision", "Observation"};
        buildTable(new Lexer().initializer("D:\\Semester 6\\Design of Compilers\\Project\\Java Project\\Lexical Analyzer\\src\\main\\java\\com\\compiler\\lexicalanalyzer\\assets\\source.txt"));
    }

    private void buildTable(List<Lexer.Token> tokens) {
        var predictiveTable = new PredictiveTable(grammar).getTable();

        table = new ArrayDeque<>();
        complete = false;
        String[] row;
        Queue<String> input = new ArrayDeque<>();
        for (Lexer.Token token : tokens) {
            if (token.Tokentype.equals("Keyword") || token.Tokentype.equals("Operator") || token.Tokentype.equals("Punctuation")) {
                input.add(token.Lexeme);
            } else {
                input.add(token.Tokentype);
            }
        }

        input.add("$");

        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push(grammar.getStartSymbol());
        System.out.println(stack.size());
        while (!stack.isEmpty()) {
            row = new String[4];
            row[0] = string(stack);
            row[1] = string(input);

            var top = stack.pop();
            var front = input.peek();
            if (isTerminal(top)) {
                if (!top.equals(front)) {
                    row[2] = "Syntax Error";
                    row[3] = "Invalid Syntax";
                    table.add(row);
                    return;
                }
                else if (front.equals("$")) {
                    row[2] = "Accepted";
                    row[3] = "Success";
                }
                else
                    row[2] = "Matching";
                input.remove();
            }
            else if (front.equals("$")) {
                var foundEmptyProduction = false;
                for (var production : grammar.getProductions(top)) {
                    var symbols = production.getSymbols();
                    if (symbols.get(0).equals(grammar.getEmptySymbol())) {
                        row[2] = "Production";
                        row[3] = symbols.toString().replaceAll("[\\[,\\]]", "");
                        foundEmptyProduction = true;
                        break;
                    }
                }
                if (!foundEmptyProduction) {
                    row[2] = "Syntax Error";
                    row[3] = "Invalid Syntax";
                    table.add(row);
                    return;
                }
            }
            else {
                if (predictiveTable.get(front) == null) {
                    row[2] = "Lexical Error";
                    row[3] = "Illegal Token";
                    table.add(row);
                    return;
                }
                var production = predictiveTable.get(front).get(top);
                System.out.println(top);
                if (production == null) {
                    row[2] = "Syntax Error";
                    row[3] = "Invalid Syntax";
                    table.add(row);
                    return;
                }
                var symbols = production.getSymbols();
                if (!symbols.get(0).equals(grammar.getEmptySymbol())) {
                    Stack<String> temp = new Stack<>();
                    for (var symbol : symbols)
                        temp.push(symbol);
                    while (!temp.isEmpty())
                        stack.push(temp.pop());
                }

                row[2] = "Production";
                row[3] = symbols.toString().replaceAll("[\\[,\\]]", "");
            }
            table.add(row);
        }
        complete = true;
    }

    public String getSource() {
        return source;
    }

    public Deque<String[]> getTable() {
        return table;
    }

    public boolean isComplete() {
        return complete;
    }

    public File getGrammarFile() {
        return grammarFile;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    private boolean isTerminal(String symbol) {
        System.out.println("Symbol: " + symbol);
        return grammar.containsTerminal(symbol);
    }

    private String string(Stack stack) {
        var string = "";
        for (var item : stack) {
            string += item + " ";
        }
        string = string.trim();
        return string;
    }

    private String string(Queue queue) {
        var string = "";
        for (var item : queue) {
            string += item + " ";
        }
        string = string.trim();
        return string;
    }
}
