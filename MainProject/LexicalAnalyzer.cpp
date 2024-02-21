#include <fstream>
#include <iostream>
#include <regex>
#include <string>
#include <vector>
#include "LexicalAnalyzer.h"
using namespace std;

void LexicalAnalyzer::tokenize(string directrory) {
    ifstream infile(directrory);
    if (!infile.good()) {
        cout << "Error opening input file" << endl;
        return;
    }

    // Define a regex pattern to match identifiers, operators, and parentheses
    // This is a simplified example; you'll need to expand it based on your requirements
    regex tokenPattern(R"((\w+)|([\(\)])|(\S))");
    string line;

    while (getline(infile, line)) {
        auto words_begin = sregex_iterator(line.begin(), line.end(), tokenPattern);
        auto words_end = sregex_iterator();

        for (sregex_iterator i = words_begin; i != words_end; ++i) {
            smatch match = *i;
            tokens.push_back(new Node(match.str(), typeOf(match.str()), typeOf(match.str()) == "id" ? 0 : -1));
        }
    }

    infile.close();
}

string LexicalAnalyzer::typeOf(string lexeme) {
    return string();
}

void LexicalAnalyzer::printTokens(vector<Token> tokens) {

}

