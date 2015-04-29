// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// The modulus operator '%' is reserved.
//
// From section 5.9 of the GLSL 1.10 spec:
//     The operator modulus (%) is reserved for future use.

#version 110

int
f() {
    int tea_time = 15 % 24;
    return tea_time;
}

