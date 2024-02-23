import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    private class Node {
        String name;
        String type;
        int index;

        Node(String name, String type) {
            this.name = name;
            this.type = type;
            this.index = -1;
        }

        Node(String name, String type, int index) {
            this.name = name;
            this.type = type;
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

    private List<Node> tokens = new ArrayList<>();
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
            //printTokens(temp2);
            // Pattern for spaces and semicolons and literals
            Pattern spaceSemiColonPattern = Pattern.compile("\"[^\"]*\"|[^ \"]+|\\n$");
            for (String token : temp2) {
                Matcher literalMatcher = spaceSemiColonPattern.matcher(token);
                while (literalMatcher.find()) {
                    String match = literalMatcher.group();
                    temp1.add(match); // Assuming typeOf for literals is "literal"
                }
            }
            temp2.clear();

            //printTokens(temp1);
            Pattern punctPattern = Pattern.compile("(\"[^\"]*\")|([()\\[\\]{},;?])|[^()\\[\\]{},;?\s]+");
            for (String token : temp1) {
                Matcher matcher = punctPattern.matcher(token);
                while (matcher.find()) {
                    String match = matcher.group();
                    temp2.add(match); // Assuming typeOf for literals is "literal"
                }
            }
            temp1.clear();
            //printTokens(temp2); // Make sure you have implemented this method
            Pattern operatorPattern = Pattern.compile("#|>>=?|<<=?|==|!=|->|<=|>=|&&|\\|\\||\\+\\+|[0-9]\\.[0-9]|--|\\+=|-=|\\*=|/=|%=|&=|\\|=|\\^=|<[^>]*>|[+\\-*/%&|.!^=<>]+");
            for (String token : temp2) {
                if (token.startsWith("\"")) {
                    // If the token starts with a double quote, add it to temp3 directly without modification
                    lexemes.add(token);
                } else {
                    Matcher matcher = operatorPattern.matcher(token);
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

            printTokens(lexemes);
        }

        catch (IOException e) {
            System.out.println("Error opening input file: " + e.getMessage());
        }
    }
//[=]+|[+]+|
    private String typeOf(String lexeme) {
        // Implementation would go here
        return ""; // Placeholder return statement
    }

    public void printTokens(List<String> t) {
        for (int i=0 ;i<t.size() ; i++) {
            System.out.println(t.get(i));
        }
    }
    public static void main(String[] args) {
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        String directory = "D:\\Semester 6\\Design of Compilers\\Project\\cpp\\test.txt";
        analyzer.tokenize(directory);

    }
}
