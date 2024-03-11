int iffy;
null->;
int x;
int xx;
bool x =true;
typedef enum {
    INT,
    FLOAT,
    CHAR
} DataType;

union {
    int intValue;
    float floatValue;
    char charValue;
}MyVariant;

struct Variant{
    DataType type;
    union MyVariant data;
}Variant;
+-+-+-+-+-1;
int 1x=1+-+-+1;
int z=+-+-+-+-+-1;
float x=-1.39434e-10;
int x=+1+1;
float y=1.1+1.2;
i+1;
i-1;
++i;
--i;
hamada.fn;
void multiplyMatrices(int mat1[][COLS1], int mat2[][COLS2], int result[][COLS2]) {
    // Multiplying matrices
    for (int i = 0; i < ROWS1; i++) {
        for (int j = 0; j < COLS2; j++) {
            result[i][j] = 0;
            for (int k = 0; k < COLS1; k++) {
                result[i][j] += mat1[i][k] * mat2[k][j];
            }
        }
    }
}
void compare(int a, int b, int c, int d) {
    // Comparing c and d
    if (c > d && a >= d) {
            printf("%d is less than %d\n", d);
    }
    // Comparing a, b, and c
    if ((a < b && b <= c) || (a == b && b == c)) {
       printf("lexical analyzer gamed gedan");
        } else{
            printf("gamed brdo 3ady");
        }
}
    
int main() {

    short item_quantity_456 = 32767;
    long long total_assets_415 = 9223372036854775807LL;
    unsigned long long database_entries_930 = 18446744073709551615ULL;
    long double speed_of_light_373839 = 3.14159265358979323846L;

    // Other data types
    _Bool is_active_404 = true;
    bool is_valid_445 = true; // using bool from stdbool.h
    void *pointer = NULL;

    // Relational operators
    int x = 10, y = 20, w = 11, z = 18;
    compare(x, y, w , z);
    printf("\n");

    bool p = 1, q = 0;
    bool and_result = p && q;
    bool or_result = !p || q;
    printf("%s\n", and_result ? "true" : "false");
    printf("%s\n", or_result ? "true" : "false");
  
    printf("p ^ q = %d\n", p ^ q, ~p);
    printf("p << 1 = %d\n", p << 1);
    printf("\n");

    int j = 2;
    do {
        printf("%d\n", j);
        j*= 2;
    } while (j <= 10);
    
    //sizeof operator
    printf("Size of int: %zu bytes\n", sizeof(int));
    
struct child { 
    int x; 
    char c; 
}; 
// parent structure declaration 
struct parent { 
    int a; 
    struct child b; 
}; 

enum Days {
    MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY
};

int num = 10;
    int *ptr;
    ptr = &num;
    printf("The address of num is: %p\n", &num);
    printf("The \"value pointed to by ptr is: %d\n", *ptr);

    return 0;
}