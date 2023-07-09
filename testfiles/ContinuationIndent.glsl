vec2 foo = vec2(
    1.0, // Should gets indented
    2.0
);

float arr[5] = float[](
    1.0, // Should gets indented
    2.0, 3.0,
    4.0, 5.0
);

void sum(vec4 a, vec4 b, out vec4 c) {
    c = a + b;
}

void main() {
    vec4 result = vec4(0.0);

    sum(
        vec4(
            1.0, // Should gets double indented
            2.0,
            3.0,
            4.0
        ),
        vec4(
            5.0,
            6.0,
            7.0,
            8.0
        ),
        result
    );
}