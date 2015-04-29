// [config]
// expect_result: fail
// glsl_version: 1.00
// [end config]
//
// The modulus assignment operator '%=' is reserved.
//
// From section 5.8 of the GLSL ES 1.00 spec:
//     The assignments remainder into (%=), left shift by (<<=), right shift
//     by (>>=), inclusive or into ( |=), and exclusive or into ( ^=) are
//     reserved for future use.

#version 100

int
f() {
    int x = 19;
    x %= 4;
    return x;
}
