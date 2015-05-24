// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// A zero modulus is legal.
//
// From section 5.9 of the GLSL 1.30 spec:
//     The resulting value [of a modulus operation] is undefined for any
//     component computed with a second operand that is zero,

#version 130

int
f() {
    int x = 1 % 0;
    return x;
}

