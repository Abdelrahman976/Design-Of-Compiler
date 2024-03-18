
//printf("printf(\"Hello, world!\\n\")");
//int main(int argc,char const*argv[]) {
//    int fortytwo = 0x2A, int0 = 0b0, int1 = 0b1, int2 = 02;
//    do { int0 += int1++ + int2; } while (--fortytwo > 1);
//    double doublewoh = (double) int0/*hi there*// (int1 += int2);
//    int hamda123hamada123 = 2;
//}
int invalid_binary_1 = 0b12; // Invalid, interpreted as octal, not binary
int invalid_binary_2 = 1b023; // Pretending to be binary but is just decimal 10

// Hexadecimal numbers
int valid_hex_1 = 0x1A3f; // Valid
int valid_hex_2 = 0X4D2; // Valid
 int invalid_hex_1 = 0x1G3; // Invalid, cannot represent in C as number
 int invalid_hex_2 = 0x4D2Z; // Invalid, cannot represent in C as number

// Octal numbers
int valid_octal_1 = 0754; // Valid
int valid_octal_2 = 042; // Valid
int invalid_octal_1 = 089; // Invalid in C, will cause a compilation error
