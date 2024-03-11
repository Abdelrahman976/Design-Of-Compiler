struct Address {
  char street[100];
  char city[50];
  int zipcode;
};
struct Employee {
  char name[50];
  int id;
  struct Address address; // Nested Address struct
};
struct Product {
  char name[50];
  int quantity;
  double price;
  // VLA for reviews (requires C99 or later)
  char reviews[quantity][100]; // Size depends on quantity
};

int add(int x, int y);
float subtract(float x, float y);
void print_message(char* message);
char* get_name(void);

// Structure declaration with various data types
struct Person {
    char name[50];
    int age;
    float height;
};

int main() {
    // Function calls
    int sum = add(5, 3);
    float difference = subtract(10.5, 2.2);
    print_message("Hello, world!");

    // Struct variable declaration and initialization
    struct Person p1;
    strcpy(p1.name, "John Doe");
    p1.age = 30;
    p1.height = 1.75;

    // Accessing struct member
    printf("Name: %s\n", p1.name);

    return 0;
}

int add(int x, int y) {
    return x + y;
}

float subtract(float x, float y) {
    return x - y;
}

void print_message(char* message) {
    printf("%s\n", message);
}

char* get_name(void) {
    char name[50];
    printf("Enter your name: ");
    fgets(name, 50, stdin);
    return name; // This is not recommended due to memory management issues. Consider returning a dynamically allocated string.
}
P1.name="M";
s2.age=20;