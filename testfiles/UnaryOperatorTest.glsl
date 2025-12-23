#version 330 core

void main() {
    // Test unary minus
    int a = -5;
    int b = -a;
    float c = -2.5;

    // Test unary plus
    int d = +5;
    int e = +a;

    // Test binary operators (should have spaces)
    int f = a + b;
    int g = a - b;

    // Test mixed
    int h = a + -b;
    int i = -a + b;
    int j = -a - -b;

    // Test in expressions
    vec3 pos = -vec3(1.0, 2.0, 3.0);
    vec3 dir = normalize(-pos);

    // Test unary not
    bool flag = !true;
    bool flag2 = !flag;

    // Test bitwise not
    int bits = ~0;
    int bits2 = ~bits;
}

