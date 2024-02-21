#include <fstream>
#include <iostream>
#include <regex>
#include <string>
#include <vector>
using namespace std;

int main() {
    ifstream infile("D:\\Semester 6\\Design of Compilers\\Project\\cpp\\test.txt");
    if (!infile.good()) {
        cout << "Error opening input file" << endl;
        return 1;
    }

    // Define a regex pattern to match identifiers, operators, and parentheses
    // This is a simplified example; you'll need to expand it based on your requirements
    regex tokenPattern(
            "\"(^(auto|break|case|char|const|continue|default|do|double|else|enum|extern|float|for|goto|if|inline|int|long|register|return|short|signed|sizeof|static|struct|switch|typedef|union|unsigned|void|volatile|while)$)\"" // Keywords
            "([+-*/%=&|^<>!?:.~]|<<|>>|==|!=|<=|>=||&&|=|+=|-=|*=|/=|%=|&=|^=||=>>=|?|:])" // Operators, escaping ++ correctly
            "|(\\b[a-zA-Z][a-zA-Z0-9]*\\b)" // Identifiers
            "|(\\b\\d+(\\.\\d+)?\\b)" // Constants (integers and basic floats)
            "|(;|,|\\(|\\)|\\{|\\})" // Punctuation (a few examples)
            "|(\".*?\")" // String literals, correct as is
            "|(\\#[^<].*)" // Preprocessor directives (simplified)
            "|(#include <[^>]+>)" // #include directives including angle brackets
    );

    string line;
    vector<string> tokens;

    while (getline(infile, line)) {
        auto words_begin = sregex_iterator(line.begin(), line.end(), tokenPattern);
        auto words_end = sregex_iterator();

        for (sregex_iterator i = words_begin; i != words_end; ++i) {
            smatch match = *i;
            tokens.push_back(match.str());
        }
    }

    infile.close();

    // Print the tokens
    for (const string& tok : tokens) {
        cout << tok << endl;
    }

    return 0;
}