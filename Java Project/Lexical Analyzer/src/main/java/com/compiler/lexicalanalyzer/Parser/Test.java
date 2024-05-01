package com.compiler.lexicalanalyzer.Parser;

import java.io.File;

public class Test {
    public Test(String grammarFileName, String sourceFileName) {
        var grammarFile = new File("D:\\Semester 6\\Design of Compilers\\Project\\Java Project\\Lexical Analyzer\\src\\main\\java\\com\\compiler\\lexicalanalyzer\\assets\\grammar.txt");
        var sourceFile = new File("D:\\Semester 6\\Design of Compilers\\Project\\Java Project\\Lexical Analyzer\\src\\main\\java\\com\\compiler\\lexicalanalyzer\\assets\\source.txt");
        var reporter = new Reporter(new AnalysisTable(grammarFile, sourceFile));

        reporter.printParsingSummary();
        reporter.printAnalysisTable();
    }
}