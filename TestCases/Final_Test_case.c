#ifdef
#elifdef
#else
#include "stdio.h"
#include <stdbool.h>
#include"ddsdsds"
/* Preprocessor directives with macros */
#define PI 3.14159
#define MAX_VALUE 100

/* Multi-line comment
   with nested single-line // comments */

//struct, union and  enum

enum  {
  KEYWORD,
  IDENTIFIER,
  INTEGER,
  OPERATOR,
  SEMICOLON,
  EOF_TOKEN
} TokenType;

typedef struct PointStruct Point;
Point p1; Point p2;

union {
    int intValue;
    float floatValue;
    char charValue;
}MyVariant;

struct Address {
  char street[100];
  char city[50];
  int zipcode;
};
struct Employee {
  char name[50];
  int id;
  struct Address address; // Nested Address struct
}

struct person{};

struct GeoShape;
// Define struct members later
struct Geoshape {
    float width;
    float height;
};

// functions & loops

int j = 2;
    do {
        printf("%d\n", j);
        j*= 2;
    } while (j <= 10);


void multiplyMatrices(int mat1[][COLS1], int mat2[][COLS2], int result[][COLS2]) {   // 2D arrays
    for (int i = 0; i < ROWS1; i++) {
        for (int j = 0; j < COLS2; j++) {
            result[i][j] = 0;
            for (int k = 0; k < COLS1; k++) {
                result[i][j] += mat1[i][k] * mat2[k][j];
            }
        }
    }
}
long long int power(int a, int b) {
    long long int result = 1;
    for (int i = 0; i < b; i++) {
        result *= a;
    }
    return result;
}

char* get_name(void) {
    char name[50];
    printf("Enter your name: ");
    fgets(name, 50, stdin);
    return name;
}

void compare(int a, int b, int c, int d) {
    //logical & relational operators
    if (c > d && a >= d) {

    }
    if ((a < b && b <= c) || (a == b && !b == c)) {
       printf("lexical analyzer gamed gedan");
        } else{
            printf("gamed brdo 3ady");
        }
}

// Switch
int check_token(const char* str, enum TokenType expected_type) {
  switch (expected_type) {
    case KEYWORD:
    //write your implementation here
      return 1;
    case IDENTIFIER:
      return 1;
    case INTEGER:
      return 1;
    case OPERATOR:
      return 1;
    case SEMICOLON:
      return strcmp(str, ";") == 0;
    case EOF_TOKEN:
      return strlen(str) == 0;
    default:
      return 0;
  }
}


int main() {
    enum {RED, GREEN, BLUE} color;
    // Declare variables
    int integerVariable = 42.3, z, h;
    int exp11 = 3451e3;
    int 0ull=5;
    int iffy;
    int 1x;
    int 1x=1+-+-+1;
    int integerTricky = +-+-+143;
    int integerTricky2 = 1 -+-+-8964;
    char tt = 'r';
    char 24611OmarVar1646461 = 'x';
    str s = "oma";
    short item_quantity_456 = 32767;
    float fVar = 3.14;
    float x=-1.39434e-10 ,y=1.1+1.2 ;-

    // Tricky Data Type testcases
    long y = 1511164.161664L;
    long long x = 3.16161111LL;
    long long total_assets_415 = 9223372036854775807LL;
    unsigned long y = 1551166ul, yy = 83166.166e-2f;
    unsigned long long database_entries_930 = 18.4467709551615ULL;
    long double speed_of_light_373 = 3.14159265358979323846L;
    _Bool is_active_404 = true;
    double sum;
    sum = integerVariable + floatVariable

    i+1;
    z-h;
    s += tt;
    h++;
    x->y->z->h = 4.5578441e-7;
    z = 3.146416e-7;
    omar.hours = 3;
    null->;
    int for = 1;
    double if = 3.14;

    //array of pointers

    const char* test_cases[] = {
    "int x = 10;",
    "if (x > 5) {",
    "  y = x + 2;",
    "}",
    "while (y < 10) {",
    "  printf(\"Hello, world!\\n\");",
    "}",
    "void main() {}",
    "float pi = 3.14;",
    "for (int i = 0; i < 10; i++) {",
    "}",
    // Add more test cases here
   };

    // Binary, Hexadecimal and octal values
    int hexValue1 = +0xABE12;
    int hexValue2 = 0xe464;
    int valid_hex_2 = 0X4D2; // Valid
    int invalid_hex = 0x1G3; // Invalid, cannot represent in C as number
    int octValue = 0123;
    int invalid_octal = 089; // Invalid in C, will cause a compilation error
    int binary = 0b0110;
    int invalid_binary_1 = 0b12; // Invalid, interpreted as octal, not binary
    int invalid_binary_2 = 1b023; // Pretending to be binary but is just decimal 10
    int floatValue = 46613e4;



    // Pointers and dereferencing
    void *pointer = NULL;
    int *ptr;
    int val = 10;
    ptr = &val;
    printf("Value using pointer: %d\n", *ptr);

    // Bitwise operators
    int a = 12, b = 25;
    int *c;
    x = a * b;
    x= a+++b;
    printf("AND: %d\n", a & b);
    printf("XOR: %d\n", a ^ b);
    printf("p << 1 = %d\n", a << 1);

    // conditional operator
    printf("%s\n" , and_result ? "true" : "false");
    printf("%s\n" , or_result ? "true" : "false");

    //sizeof operator
    printf("Size of int: %zu bytes\n", sizeof(int));

    // function calls
    int x = 10, y = 20, w = 11, z = 18;
    compare(x, y, w , z);
    int base = 2;
    int exponent = 5;
    long long int result_power = power(base, exponent);
    void print_message(char* message);
    char* get_name(void);

    // Declare and initialize a struct Address variable
    struct Address myAddress = {
        .street = "123 Main Street",
        .city = "Cityville"
    };
    myAddress.zipcode= 1234

  // more tricky code
    int fortytwo = 0x2A, int0 = 0b0, int1 = 0b1, int2 = 02;
    do { int0 += int1++ + int2; } while (--fortytwo > 1);
    double doublewoh = (double) int0/*hi there*// (int1 += int2);
    printf("printf(\"Hello, world!\\n\")");
    int hello123hello123=2;
    // Test inside double quotes
  printf("The \"value pointed to by\" ptr is: %d\n", *ptr);
  // Test bad code
   char x = 'a;
   char y = a';
    // String with escape sequences
    char message[] = "Hello, world!\n\tThis is a test.";
    return 0;
}