package com.compiler.lexicalanalyzer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Paths;
public class LexicalAnalyzer {
    protected class Token {
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
        public String getLexeme() {
            return Lexeme;
        }
    }
    private class SymbolTable {
        String name;
        String type;
        String ID;
        int size;
        ArrayList<Integer> line_of_refrences = new ArrayList<>();
        SymbolTable(String name, String type,String ID, int size) {
            this.name = name;
            this.type = type;
            this.ID = ID;
            this.size = size;
        }
    }

    private  List<Token> tokens = new ArrayList<>();
    private  List<String> lexemes = new ArrayList<>();
    private List<SymbolTable> symbolTableRow = new ArrayList<>();
    BufferedReader RemoveCommentsLibraries(String path) throws IOException {
        // Read the entire file as bytes
        byte[] fileBytes = Files.readAllBytes(Paths.get(path));
        // Convert bytes to string if necessary
        String fileContent = new String(fileBytes); // Use the appropriate character encoding if known
        fileContent = fileContent.replaceAll("//.*|(?s)/\\*.*?\\*/", ""); // remove comments
        fileContent = fileContent.replaceAll("#include\\s*[\"<][^\">]+[\">](\\r?\\n|\\r)", "");// remove libraries

        return new BufferedReader(new StringReader(fileContent));
    }

    public void tokenize(String directory) {
        try (BufferedReader infile = RemoveCommentsLibraries(directory)){
            String line;
            List<String> temp1 = new ArrayList<>();
            List<String> temp2 = new ArrayList<>();
            while ((line = infile.readLine()) != null) {
                temp2.add(line);
            }
//            for (int i = 0; i < temp2.size(); i++) {
//                System.out.println(temp2.get(i));
//            }
            Pattern Filter1 = Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\"|^\\\"|\"[^\"]*\"|[^ \"]+|\\n$|\"");
            TokenizeHelper(Filter1, temp2, temp1);
            temp2.clear();
//            for (int i = 0; i < temp1.size(); i++) {
//                System.out.println(temp1.get(i));
//            }
            //printTokens(temp1);
            Pattern Filter2 = Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\"|([()\\[\\]{},;?])|[^()\\[\\]{},;? ]+");
            TokenizeHelper(Filter2, temp1, temp2);
            temp1.clear();
            //printTokens(temp2);
            Pattern Filter3 = Pattern.compile(
                    "[0-9]+[_a-zA-Z][_a-zA-Z0-9]*|[0-9]+\\.?[0-9]*([eE][-+]?\\d+)?(f|F|ULL|ull|LL|ll|UL|ul|L|l|u|U)|0[xX][0-9a-fA-F]+|0[0-7]+|0[bB][01]+|[_a-zA-Z]+[0-9]*|>>=?|<<=?|==?|!=|->|<=|>=|&&|\\|\\||[/%&|.!^<>]|([0-9]*[.])?[0-9]+([eE][-+]?\\d+)?|[0-9]+f|((\\+-)+\\+?|(-\\+)+-?)(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][-+]?\\d+)?"
                    +"|\\+\\+|\\+|--|-|\\+=|-=|\\*=|/=|%=|&=|\\|=|\\^=|\\*|"
                    +"#|<[^>]*>"
            );

            FilteringHelper(Filter3,temp2);
//            for (int i = 0; i < lexemes.size(); i++) {
//                System.out.println(lexemes.get(i));
//            }
        }
        catch (IOException e) {
            System.out.println("Error opening input file: " + e.getMessage());
        }
    }
    private void TokenizeHelper(Pattern x, List<String> y, List<String> z) {
        for (String token : y) {
            Matcher matcher = x.matcher(token);
            while (matcher.find()) {
                String match = matcher.group();
                z.add(match);
            }
        }
    }
    private void FilteringHelper(Pattern x,List<String> y) {
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
    }
    public void typeOf() {
        String stringsPattern = "\".*\""; // Matches string literals
        String charsPattern = "'.?'"; // Matches char literals
        String operatorsPattern = "~|:|#|\\*|>>=?|<<=?|==|!=|->|<=|>=|&&|\\|\\||\\+\\+|--|\\+=|-=|\\*=|/=|%=|&=|\\|=|\\^=|<[^>]*>|[+\\-/%&|.!^=<>]+"; // Simple example for common operators
        String punctuationPattern = "([()\\[\\]{},;?\"])"; // Common punctuation marks
        String LongPattern= "[0-9]+\\.?[0-9]*(L|UL|l|ul)"; // Matches long numbers
        String LongLongPattern= "[0-9]+\\.?[0-9]*(ULL|LL|ull|ll)"; // Matches long numbers
        String integersPattern = "[-+]?[0-9]+|0[xX][0-9a-fA-F]+|0[0-7]+|0[bB][01]+|((\\+-)*\\+?|(-\\+)*-?)(0|[1-9][0-9]*)"; // Matches integer numbers
        String floatsPattern = "[-+]?([0-9]*[.])?[0-9]+([eE][-+]?\\d+)?([fF])|[0-9]+f|((-\\+)*-?)(0|[1-9][0-9]*[.])?[0-9]+([eE][-+]?\\d+)?"; // Matches floating-point numbers
        String keywordsPattern = "(auto|break|case|char|const|continue|default|do|double|else|enum|extern|float|for|goto|if|inline|int|long|register|restrict|return|short|signed|sizeof|static|struct|switch|typedef|union|unsigned|void|volatile|while)";
        String identifier = "[_a-zA-Z][_a-zA-Z0-9]*"; // identifier
//        String BadStringPattern = "[0-9]+[_a-zA-Z][_a-zA-Z0-9]*"; // Matches badString literals
        boolean StructId = false;
        boolean UnionId = false;
        boolean EnumId = false;
        boolean skip = false;
        boolean sign = false;
        int signCounter = 0;
        for (int i=0;i<lexemes.size();i++){
            if(skip){
                skip = false;
                continue;
            }
            if(i==signCounter){
                sign = false;
            }
            if(lexemes.get(i).matches("enum"))
                EnumId = true;
            if (lexemes.get(i).matches("struct"))
                StructId = true;
            if (lexemes.get(i).matches("union"))
                UnionId = true;
            if((lexemes.get(i).matches("((\\+-)+\\+?|(-\\+)+-?)(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][-+]?\\d+)?"))&& i!=0 &&!sign &&!lexemes.get(i-1).matches("=|\\+=|-=|\\*=|/=|%=|&=|\\|=|\\^=")){
                sign = true;
                String lexeme = lexemes.get(i);
                String firstSign = lexeme.substring(0, 1); //the first sign
                String withoutFirstSign = lexeme.substring(1); // Cut out the first sign
                lexemes.set(i, withoutFirstSign); // Update the list with the modified string
                tokens.add(new Token(firstSign, "Operator"));
                signCounter=i+1;
                i--;
            }
            else if (lexemes.get(i).matches(stringsPattern))
                tokens.add(new Token(lexemes.get(i), "String Literal"));
             else if (lexemes.get(i).matches(charsPattern)) {
                tokens.add(new Token(lexemes.get(i), "Char Literal"));
            } else if (lexemes.get(i).matches(operatorsPattern)) {
                if (i>1&&lexemes.get(i).matches("[+|-]")&&lexemes.get(i-1).matches("=")&&(lexemes.get(i+1).matches(integersPattern)||lexemes.get(i+1).matches(floatsPattern))){
                    skip = true;
                    String concatenated = lexemes.get(i) + lexemes.get(i+1); // Concatenate strings
                    lexemes.set(i, concatenated);
                    if(lexemes.get(i+1).matches(integersPattern))
                        tokens.add(new Token(lexemes.get(i), "Integer"));
                    else if(lexemes.get(i+1).matches(floatsPattern))
                        tokens.add(new Token(lexemes.get(i), "Float"));
                    else if (lexemes.get(i+1).matches(LongLongPattern))
                        tokens.add(new Token(lexemes.get(i), "Long Long"));
                    else if (lexemes.get(i+1).matches(LongPattern))
                        tokens.add(new Token(lexemes.get(i), "Long"));
                } else
                    tokens.add(new Token(lexemes.get(i), "Operator"));
            } else if (lexemes.get(i).matches(punctuationPattern)) {
                tokens.add(new Token(lexemes.get(i), "Punctuation"));
            } else if (lexemes.get(i).matches(LongLongPattern)) {
                tokens.add(new Token(lexemes.get(i), "Long Long"));
            } else if (lexemes.get(i).matches(LongPattern)) {
                tokens.add(new Token(lexemes.get(i), "Long"));
            } else if (lexemes.get(i).matches(integersPattern)) {
                tokens.add(new Token(lexemes.get(i), "Integer"));
            } else if (lexemes.get(i).matches(floatsPattern)) {
                tokens.add(new Token(lexemes.get(i), "Float"));
            } else if (lexemes.get(i).matches(keywordsPattern)) {
                tokens.add(new Token(lexemes.get(i), "Keyword"));//
            }
             else if (lexemes.get(i).matches(identifier)) {
                tokens.add(new Token(lexemes.get(i), "Identifier"));
                if(EnumId && lexemes.get(i-1).matches("enum")||(EnumId && lexemes.get(i-1).matches("}")&& lexemes.get(i+1).matches(";"))) {
                    tokens.getLast().IdType = "Enum";
                    EnumId=false;
                } else if ( UnionId && lexemes.get(i-1).matches("union") || (UnionId && lexemes.get(i-1).matches("}") && lexemes.get(i+1).matches(";"))) {
                    tokens.getLast().IdType = "Union";
                    UnionId = false;
                } else if (lexemes.get(i+1).matches("\\(")) {
                    tokens.getLast().IdType = "Function";
                }
                else if (i!=0 &&((StructId && lexemes.get(i-1).matches("struct")) || (StructId && lexemes.get(i-1).matches("}") && lexemes.get(i+1).matches(";")))) {
                    tokens.getLast().IdType = "Struct";
                    StructId = false;
                }
                else if(i!=0 && lexemes.get(i-1).matches("typedef") && lexemes.get(i+1).matches(";")){
                    tokens.getLast().IdType = "Typedef";
                } else if (i!=0 && (lexemes.get(i-1).matches("int|float|char|double|long|short|string") || lexemes.get(i-1).matches(identifier))){
                    tokens.getLast().IdType = "Variable";
                }
            } else  {
                tokens.add(new Token(lexemes.get(i), "BadString"));
            }
        }
    }

    public void printTokens() {
        for (Token token : tokens) {
            System.out.println("<" + token.Tokentype + "," + token.Lexeme + ">" + " ");
            if (token.IdType != null) {
                System.out.println("ID Type: " + token.IdType + "\n");
            }
        }
    }
    public void SymbolTableMaker(String directory){
        int counter=1;
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.Tokentype.equals("Identifier")) {
                boolean exists = false;
                for (SymbolTable symbol : symbolTableRow) {
                    if (symbol.name.equals(token.Lexeme)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    symbolTableRow.add(new SymbolTable(token.Lexeme, token.IdType,"id".concat(String.valueOf(counter++)),0));
                }
            }
        }
        try (BufferedReader infile = new BufferedReader(new FileReader(directory))){
            String line;
            int lineNumber = 0;
            boolean inBlockComment = false;
            while ((line = infile.readLine()) != null) {
                lineNumber++;
                if (inBlockComment) {
                    if (line.contains("*/")) {
                        inBlockComment = false;
                        String[] parts = line.split("\\*/", -1);
                        if (parts.length > 0) {
                            line = parts[0]; // Keep only the part before the end of the block comment
                        }
                    }
                } else {
                    String[] blockParts = line.split("/\\*", -1);
                    if (blockParts.length > 1) {
                        inBlockComment = true;
                        line = blockParts[0]; // Keep only the part before the start of the block comment
                    }

                    String[] inlineParts = line.split("//", -1);
                    if (inlineParts.length > 0) {
                        line = inlineParts[0]; // Keep only the part before the inline comment
                    }
                }

                for (SymbolTable symbol : symbolTableRow) {
                    // Create a pattern that matches the symbol name as a standalone word
                    Pattern pattern = Pattern.compile("(?<!%)\\b" + symbol.name + "\\b");
                    Matcher matcher = pattern.matcher(line);
                    // If the symbol name is found as a standalone word, add the line number
                    if (matcher.find()) {
                        symbol.line_of_refrences.add(lineNumber);
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error opening input file: " + e.getMessage());
        }
    }
    public void printSymbolTable() {
        // Header
        String headerFormat = "%-8s %-25s %-15s %-10s %-25s %-20s\n";
        System.out.printf(headerFormat, "ID", "Name", "Type", "Size", "Line of Declaration", "Lines of References");

        // Data rows
        String rowFormat = "%-8s %-25s %-15s %-10d %-25d ";

        for (SymbolTable symbol : symbolTableRow) {
            // Check if the symbol has any lines of references before attempting to print
            if(!symbol.line_of_refrences.isEmpty()) {
                // Print the first part of the row including the ID, name, type, size, and the first line of declaration
                System.out.printf(rowFormat, symbol.ID, symbol.name, symbol.type, symbol.size, symbol.line_of_refrences.getFirst());
            }
            for (int line=1;line<symbol.line_of_refrences.size(); line++)
                System.out.print(symbol.line_of_refrences.get(line) + " ");

            // After printing all lines of references for the current symbol, move to the next line
            System.out.println();
        }
    }
    public static void main(String[] args)  {
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        String directory = "D:\\Maeen\\UNI Documents\\OneDrive - Faculty of Engineering Ain Shams University\\Junior\\Spring 2024\\Design of Compilers\\Project\\Design-Of-Compiler\\test.txt";
        analyzer.tokenize(directory);
        analyzer.typeOf();
        analyzer.printTokens();
        //analyzer.SymbolTableMaker(directory);
        //analyzer.printSymbolTable();
    }
}
