cmake_minimum_required(VERSION 3.26)
project(MainProject)

set(CMAKE_CXX_STANDARD 23)

add_executable(MainProject main.cpp
        LexicalAnalyzer.h
        LexicalAnalyzer.cpp)
