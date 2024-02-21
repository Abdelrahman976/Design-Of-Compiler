#include <iostream>
#include "vector"
using namespace std;
class LexicalAnalyzer{
    private:
        class Node{
        public:
            string name;
            string type;
            int index;
            Node(string n, string t):name(n), type(t), index(-1) {}
            Node(string n, string t, int i):name(n), type(t), index(i) {}
        };
        typedef Node *Token;
        vector <Token> tokens;
        //vector for symbol table, fn to make it and to print it
    public:
        LexicalAnalyzer();
        string typeOf(string lexeme);
        void tokenize(string directrory);
        void printTokens(vector<Token> tokens);
};