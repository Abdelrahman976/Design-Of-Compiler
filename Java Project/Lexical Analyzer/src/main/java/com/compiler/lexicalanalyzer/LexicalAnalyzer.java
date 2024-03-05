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

    private final List<Token> tokens = new ArrayList<>();
    private final List<String> lexemes = new ArrayList<>();
    BufferedReader RemoveComments(String path) throws IOException {
        // Read the entire file as bytes
        byte[] fileBytes = Files.readAllBytes(Paths.get(path));
        // Convert bytes to string if necessary
        String fileContent = new String(fileBytes); // Use the appropriate character encoding if known
        fileContent = fileContent.replaceAll("//.*|(?s)/\\*.*?\\*/", ""); // remove comments
        return new BufferedReader(new StringReader(fileContent));
    }

    public void tokenize(String directory) {
        try (BufferedReader infile = RemoveComments(directory)){
            String line;
            List<String> temp1 = new ArrayList<>();
            List<String> temp2 = new ArrayList<>();
            while ((line = infile.readLine()) != null) {
                temp2.add(line);
            }
//            for (int i = 0; i < temp2.size(); i++) {
//                System.out.println(temp2.get(i));
//            }
            // Pattern for spaces and semicolons and literals
            Pattern spaceSemiColonPattern = Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\"|^\\\"|\"[^\"]*\"|[^ \"]+|\\n$");
            TokenizeHelper(spaceSemiColonPattern, temp2, temp1);
            temp2.clear();
//            for (int i = 0; i < temp1.size(); i++) {
//                System.out.println(temp1.get(i));
//            }
            //printTokens(temp1);
            Pattern punctPattern = Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\"|([()\\[\\]{},;?])|[^()\\[\\]{},;? ]+");
            TokenizeHelper(punctPattern, temp1, temp2);
            temp1.clear();
            //printTokens(temp2);
            Pattern operatorPattern = Pattern.compile(
                    "[0-9]+\\.?[0-9]*(f|F|ULL|ull|LL|ll|u|U|L|l|UL|ul)|0[xX][0-9a-fA-F]+|0[0-7]+|0[bB][01]+|[_a-zA-Z]+[0-9]*|>>=?|<<=?|==?|!=|->|<=|>=|&&|\\|\\||[/%&|.!^<>]|([0-9]*[.])?[0-9]+([eE][-+]?\\d+)?|[0-9]+f|((\\+-)+\\+?|(-\\+)+-?)(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][-+]?\\d+)?"
                        // Matches identifiers, numbers, and logical operators
                    +"|\\+\\+|\\+|--|-|\\+=|-=|\\*=|/=|%=|&=|\\|=|\\^=|\\*|" //Matches arithmetic operators
                    +"#|<[^>]*>" // Matches Library definitions
            );

            OperatorPatternHelper(operatorPattern,temp2);
//            for (int i = 0; i < lexemes.size(); i++) {
//                System.out.println(lexemes.get(i));
//            }
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
    private void OperatorPatternHelper(Pattern x,List<String> y) {
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
        String LongPattern= "[0-9]+\\.?[0-9]*(L|UL|l|ul)"; // Matches long numbers
        String LongLongPattern= "[0-9]+\\.?[0-9]*(ULL|LL|ull|ll)"; // Matches long numbers
        String integersPattern = "[-+]?[0-9]+|0[xX][0-9a-fA-F]+|0[0-7]+|0[bB][01]+|((\\+-)*\\+?|(-\\+)*-?)(0|[1-9][0-9]*)"; // Matches integer numbers
        String floatsPattern = "[-+]?([0-9]*[.])?[0-9]+([eE][-+]?\\d+)?|[0-9]+f|((-\\+)*-?)(0|[1-9][0-9]*[.])?[0-9]+([eE][-+]?\\d+)?"; // Matches floating-point numbers
        String keywordsPattern = "(auto|break|case|char|const|continue|default|do|double|else|enum|extern|float|for|goto|if|inline|int|long|register|restrict|return|short|signed|sizeof|static|struct|switch|typedef|union|unsigned|void|volatile|while)";
        String identifier = "[_a-zA-Z][_a-zA-Z0-9]*"; // identifier
        boolean StructId = false;
        boolean UnionId = false;
        boolean EnumId = false;
        boolean skip = false;
        for (int i=0;i<lexemes.size();i++){
            if(skip){
                skip = false;
                continue;
            }
            if(lexemes.get(i).matches("enum"))
                EnumId = true;
            if (lexemes.get(i).matches("struct"))
                StructId = true;
            if (lexemes.get(i).matches("union"))
                UnionId = true;
            if (lexemes.get(i).matches(stringsPattern)) {
                tokens.add(new Token(lexemes.get(i), "String Literal"));
            } else if (lexemes.get(i).matches(charsPattern)) {
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
                } else {
                tokens.add(new Token(lexemes.get(i), "Operator"));}

            } else if (lexemes.get(i).matches(punctuationPattern)) {
                tokens.add(new Token(lexemes.get(i), "Punctuation"));
            } else if (lexemes.get(i).matches(LongLongPattern)) {
                tokens.add(new Token(lexemes.get(i), "Long Long"));
            } else if (lexemes.get(i).matches(LongPattern)) {
                tokens.add(new Token(lexemes.get(i), "Long"));
            } else if (lexemes.get(i).matches(integersPattern)) {
//                if((lexemes.get(i).matches("\\+")&&lexemes.get(i+1).matches("-")&&(lexemes.get(i-1).matches(integersPattern)||lexemes.get(i-1).matches(floatsPattern))))  {
//                    String lexeme = lexemes.get(i);
//                    int plusIndex = lexeme.indexOf('+'); // Find the index of the first plus sign
//                    String withoutFirstPlus = lexeme.substring(plusIndex + 1); // Cut out the first plus
//                    lexemes.set(i, withoutFirstPlus); // Update the list with the modified string
//                    tokens.add(new Token("+", "Operator"));
//                    i--;
//                } else if((lexemes.get(i).matches("-")&&lexemes.get(i+1).matches("\\+")&&(lexemes.get(i-1).matches(integersPattern)||lexemes.get(i-1).matches(floatsPattern)))) {
//                    String lexeme = lexemes.get(i);
//                    int minusIndex = lexeme.indexOf('-'); // Find the index of the first plus sign
//                    String withoutFirstMinus = lexeme.substring(minusIndex + 1); // Cut out the first plus
//                    lexemes.set(i, withoutFirstMinus); // Update the list with the modified string
//                    tokens.add(new Token("-", "Operator"));
//                    i--;
//                }
//                else
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
                else if ((StructId && lexemes.get(i-1).matches("struct")) || (StructId && lexemes.get(i-1).matches("}") && lexemes.get(i+1).matches(";"))) {
                    tokens.getLast().IdType = "Struct";
                    StructId = false;
                }
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
    public static void main(String[] args)  {
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        String directory = "D:\\Semester 6\\Design of Compilers\\Project\\MainTest.c";
        analyzer.tokenize(directory);
        analyzer.typeOf();
       analyzer.printTokens();

    }
}
