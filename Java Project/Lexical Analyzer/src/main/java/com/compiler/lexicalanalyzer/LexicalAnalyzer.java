package com.compiler.lexicalanalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    private class Token {
        String Lexeme;
        String Tokentype;
        String IdType;
        int index;

        Token(String name, String type) {
            this.Lexeme = name;
            this.Tokentype = type;
            this.index = -1;
        }

        Token(String name, String type, int index) {
            this.Lexeme = name;
            this.Tokentype = type;
            this.index = index;
        }
    }
    private class SymbolTable {
        String name;
        String type;
        int size;
        int line_of_declaration;
        ArrayList<Integer> line_of_refrences = new ArrayList<>();
        SymbolTable(String name, String type, int size, int line_number) {
            this.name = name;
            this.type = type;
            this.size = size;
            this.line_of_declaration = line_number;
        }
    }

    private List<Token> tokens = new ArrayList<>();
    private List<String> lexemes = new ArrayList<>();
    public void tokenize(String directory) {
        try (BufferedReader infile = new BufferedReader(new FileReader(directory))) {
            String line;
            List<String> temp1 = new ArrayList<>();
            List<String> temp2 = new ArrayList<>();
            Pattern commentsPattern = Pattern.compile("//.*");
            while ((line = infile.readLine()) != null) {
                Matcher commentsMatcher = commentsPattern.matcher(line);
                while (commentsMatcher.find()) {
                    String match = commentsMatcher.group();
                    line = line.replace(match, "");
                }
                temp2.add(line);
            }
            // Pattern for spaces and semicolons and literals
            Pattern spaceSemiColonPattern = Pattern.compile("\"[^\"]*\"|[^ \"]+|\\n$");
            temp1 = TokenizeHelper(spaceSemiColonPattern,temp2,temp1);
            temp2.clear();
            //printTokens(temp1);
            Pattern punctPattern = Pattern.compile("(\"[^\"]*\")|([()\\[\\]{},;?])|[^()\\[\\]{},;?\s]+");
            temp2 = TokenizeHelper(punctPattern,temp1,temp2);
            temp1.clear();
            //printTokens(temp2);
            Pattern operatorPattern = Pattern.compile(
                    "[_a-zA-Z]+[0-9]*|[/%&|.!^<>]|\\*|>>=?|<<=?|==?|!=|->|<=|>=|&&|\\|\\||[+\\-]?([0-9]*[.])?[0-9]+([eE][-+]?\\d+)?"
                        // Matches identifiers, numbers, and logical operators
                    +"|\\+\\+|\\+|--|-|\\+=|-=|\\*=|/=|%=|&=|\\|=|\\^=|" //Matches arithmetic operators
                    +"#|<[^>]*>" // Matches Library definitions
            );

            OperatorPattern(operatorPattern,temp2);
            for (int i = 0; i < lexemes.size(); i++) {
                System.out.println(lexemes.get(i));
            }
        }
        catch (IOException e) {
            System.out.println("Error opening input file: " + e.getMessage());
        }
    }
    private List<String> TokenizeHelper(Pattern x,List<String> y,List<String> z) {
        for (String token : y) {
            Matcher matcher = x.matcher(token);
            while (matcher.find()) {
                String match = matcher.group();
                z.add(match);
            }
        }
        return z;
    }
    private void OperatorPattern(Pattern x,List<String> y) {
        for (String token : y) {
            if (token.startsWith("\"")) {
                // If the token starts with a double quote, add it to temp3 directly without modification
                lexemes.add(token);
            } else {
                Matcher matcher = x.matcher(token);
                int lastEnd = 0;
                while (matcher.find()) {
                    if (matcher.start() > lastEnd) {
                        // Add non-operator token that precedes the operator
                        lexemes.add(token.substring(lastEnd, matcher.start()));
                    }
                    // Add the operator token
                    lexemes.add(matcher.group());
                    lastEnd = matcher.end();
                }
                // Add any remaining text after the last match as a token
                if (lastEnd < token.length()) {
                    lexemes.add(token.substring(lastEnd));
                }
            }
        }
//        for (int i = 0; i < lexemes.size(); i++) {
//            System.out.println(lexemes.get(i));
//        }
    }
    public void typeOf() {
        String stringsPattern = "\".*\""; // Matches string literals
        String charsPattern = "'.?'"; // Matches char literals
        String operatorsPattern = "#|\\*|>>=?|<<=?|==|!=|->|<=|>=|&&|\\|\\||\\+\\+|--|\\+=|-=|\\*=|/=|%=|&=|\\|=|\\^=|<[^>]*>|[+\\-/%&|.!^=<>]+"; // Simple example for common operators
        String punctuationPattern = "([()\\[\\]{},;?])"; // Common punctuation marks
        String integersPattern = "[-+]?[0-9]+"; // Matches integer numbers
        String floatsPattern = "[-+]?([0-9]*[.])?[0-9]+([eE][-+]?\\d+)?"; // Matches floating-point numbers
        String keywordsPattern = "(auto|break|case|char|const|continue|default|do|double|else|enum|extern|float|for|goto|if|inline|int|long|register|restrict|return|short|signed|sizeof|static|struct|switch|typedef|union|unsigned|void|volatile|while)";
        String identifier = "[_a-zA-Z][_a-zA-Z0-9]*"; // identifier
        String VarIdPattern = "int|float|char|double|long|short|signed|unsigned|void";
        boolean VarId = false;
        boolean StructId = false;
        for (int i=0;i<lexemes.size();i++){
            if (lexemes.get(i).matches(VarIdPattern)) {
                VarId = true;
            } if (lexemes.get(i).matches("struct")) {
                StructId = true;
            }
            if (lexemes.get(i).matches(stringsPattern)) {
                tokens.add(new Token(lexemes.get(i), "String"));
            } else if (lexemes.get(i).matches(charsPattern)) {
                tokens.add(new Token(lexemes.get(i), "Char"));
            } else if (lexemes.get(i).matches(operatorsPattern)) {
                tokens.add(new Token(lexemes.get(i), "Operator"));
            } else if (lexemes.get(i).matches(punctuationPattern)) {
                tokens.add(new Token(lexemes.get(i), "Punctuation"));
            } else if (lexemes.get(i).matches(integersPattern)) {
                tokens.add(new Token(lexemes.get(i), "Integer"));
            } else if (lexemes.get(i).matches(floatsPattern)) {
                tokens.add(new Token(lexemes.get(i), "Float"));
            } else if (lexemes.get(i).matches(keywordsPattern)) {
                tokens.add(new Token(lexemes.get(i), "Keyword"));//
            }
             else if (lexemes.get(i).matches(identifier)) {
                tokens.add(new Token(lexemes.get(i), "Identifier"));
                if (lexemes.get(i+1).matches("\\(")) {
                    tokens.get(tokens.size() - 1).IdType = "Function";
                }
                else if (VarId) {
                    tokens.get(tokens.size() - 1).IdType = "Variable";
                    VarId = false;
                }
                else if ((StructId && lexemes.get(i-1).matches("struct")) || (StructId && lexemes.get(i-1).matches("}") && lexemes.get(i+1).matches(";"))) {
                    tokens.get(tokens.size() - 1).IdType = "Struct";
                    StructId = false;
                }
            }
        }
    }

    public void printTokens() {
        for (int i=0 ;i<tokens.size() ; i++) {
            System.out.println("<" + tokens.get(i).Tokentype + "," + tokens.get(i).Lexeme + ">"+ " ");
            if(tokens.get(i).IdType != null) {
                System.out.println("ID Type: " + tokens.get(i).IdType+ "\n");
            }
        }
    }
    public static void main(String[] args) {
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        String directory = "D:\\Maeen\\UNI Documents\\OneDrive - Faculty of Engineering Ain Shams University\\Junior\\Spring 2024\\Design of Compilers\\Project\\Design-Of-Compiler\\test.txt";
        analyzer.tokenize(directory);
        analyzer.typeOf();
//        for(int i=0;i<analyzer.tokens.size();i++){
//            System.out.println(analyzer.tokens.get(i).Lexeme );
//        }
//        analyzer.printTokens();

    }
}
