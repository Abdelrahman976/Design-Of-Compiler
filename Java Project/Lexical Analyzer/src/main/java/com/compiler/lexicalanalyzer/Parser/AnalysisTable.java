package com.compiler.lexicalanalyzer.Parser;

import com.compiler.lexicalanalyzer.LexicalAnalyzer;
import org.abego.treelayout.demo.TextInBox;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

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
    public DefaultTreeForTreeLayout<TextInBox> tree;

    public AnalysisTable(File grammarFile, File sourceFile) {
        this.grammarFile = grammarFile;
        this.sourceFile = sourceFile;
        grammar = new Grammar(grammarFile);
        source = PreProcessor.getStrippedSource(sourceFile);
        columnNames = new String[]{"Stack", "Tokens Queue", "Decision", "Observation"};
        buildTable(new Lexer().initializer("src/main/java/com/compiler/lexicalanalyzer/assets/source.txt"));
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
                if (token.Tokentype.equals("String Literal"))
                    input.add("String_Literal");
                else if (token.Tokentype.equals("Char Literal"))
                    input.add("Char_Literal");
                else if (token.Tokentype.equals("Long Long")) {
                    input.add("Long_Long");
                } else
                input.add(token.Tokentype);
            }
        }

        input.add("$");

        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push(grammar.getStartSymbol());

        var nodeStack = new Stack<>();

        // Create a TextInBox object for the root of your tree
        TextInBox root = new TextInBox(grammar.getStartSymbol(), 80, 20);

        // Create a DefaultTreeForTreeLayout object with the root
         tree = new DefaultTreeForTreeLayout<>(root);

        // Push the root node onto the node stack
        nodeStack.push(root);
        TextInBox parentNode = (TextInBox) nodeStack.peek(); // Keep track of the parent node
        var childrenStack = new Stack<>(); // Stack to store children nodes
//        System.out.println(stack.size());
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
                if (production == null) {
                    row[2] = "Syntax Error";
                    row[3] = "Invalid Syntax";
                    table.add(row);
                    return;
                }
                var symbols = production.getSymbols();
                while (!childrenStack.isEmpty()) {
                    TextInBox potentialParent = (TextInBox) childrenStack.pop();
                    // If the potential parent has a non-epsilon production, update the parent node
                    if (!Objects.equals(row[3], "epsilon")) {
                        parentNode = potentialParent;
                        break; // Exit the loop once a suitable parent is found
                    }
                }
                if (!symbols.get(0).equals(grammar.getEmptySymbol())) {
                    Stack<String> temp = new Stack<>();
                    for (var symbol : symbols)
                        temp.push(symbol);

                    Stack<String> tempReverse = (Stack<String>) temp.clone();
                    Collections.reverse(tempReverse);
                    while (!temp.isEmpty()) {
                        String symbol = temp.pop();
                        stack.push(symbol);

                        //String symbol2 = tempReverse.pop();
                        // Create a TextInBox object with the symbol
                        TextInBox node = new TextInBox(symbol, symbol.length() * 10, 20);

                        // Add this object as a child to the parent node
                        tree.addChild(parentNode, node);

                        // If the symbol has a production, update the parent node
                        if (grammar.getNonTerminals().contains(symbol)) {
                            childrenStack.push(node); // Update the parent node
                        }
                        // Push the new node onto the node stack
                        nodeStack.push(node);
                    }

                }
                if (symbols.get(0).equals(grammar.getEmptySymbol())) {
                    // Create a TextInBox object with the epsilon symbol
                    TextInBox epsilonNode = new TextInBox("ε", 20, 20);

                    // Add the epsilon node as a child to the parent node
                    tree.addChild(parentNode, epsilonNode);

                    // Push the epsilon node onto the node stack
                    nodeStack.push(epsilonNode);
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
//        System.out.println("Symbol: " + symbol);
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
