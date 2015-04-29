// [config]
// expect_result: fail
// glsl_version: 1.00
// [end config]
//
// The modulus operator '%' is reserved.
//
// From section 5.9 of the GLSL ES 1.00 spec:
//     The operator remainder (%) is reserved for future use.

#version 100

int
f() {
    int tea_time = 15 % 24;
    return tea_time;
}
