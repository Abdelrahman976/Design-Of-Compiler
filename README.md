# C Programming Language Compiler Project

## Project Overview

This project is focused on developing the initial phases of a compiler for the C programming language, specifically targeting the Lexical Analysis and Syntax Analysis stages. Designed as a collaborative effort by a team of 5 students, our goal is to create a robust tool capable of processing C code, identifying its basic constructs, and ensuring syntactic correctness as per the language specifications.

## Language Specifications

The compiler aims to recognize and handle the following constructs of the C programming language:

### Keywords

The reserved words in C used to identify variable types, control flow, and other language constructs.

### Variable Identifiers

Names assigned to variables for storing values of various data types.

### Function Identifiers

Names assigned to functions for encapsulating reusable blocks of code.

### Data Types

The various types of data that can be manipulated within C, including integers, floats, characters, and more.

### Functions

Blocks of code designed to perform specific tasks, identified by function identifiers.

### Statements

- **Assignment Statement:** Assigns a value to a variable.
- **Declaration Statement:** Declares a variable, specifying its type.
- **Return Statement:** Ends the execution of a function and returns control to the calling function.
- **Iterative Statement:** Facilitates the execution of a block of statements repeatedly.
- **Conditional Statements:** Directs the flow of execution based on certain conditions.
- **Function Call Statement:** Invokes a function.

### Expressions

- **Arithmetic:** Represents numeric calculations.
- **Boolean:** Evaluates to either true or false, guiding the flow control.

## Project Components

### Lexical Analysis (Lexer)

Implemented in Java, the lexer categorizes the input source code into tokens based on the C programming language specifications. This is the first step in understanding the syntactic structure of the code.

### Syntax Analysis (Parser)

The parser takes the tokens generated by the lexer and constructs a Parse Tree, ensuring syntactic correctness of the code or reporting errors. Key aspects of this phase include:

- **Adding Grammar Rules:** Defining the syntactic rules of the C programming language.
- **Creating the Symbol Table:** A data structure used by the compiler to store information about the source program's symbols.
- **Adding the Parse Tree:** A hierarchical structure that represents the syntactic structure of the parsed code.
