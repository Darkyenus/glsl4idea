// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// The modulus assignment operator '%=' is reserved.
//
// From section 5.8 of the GLSL 1.10 spec:
//     The assignments modulus into (%=), left shift by (<<=), right shift by
//     (>>=), inclusive or into ( |=), and exclusive or into ( ^=). These
//     operators are reserved for future use.


#version 110

int
f() {
    int x = 19;
    x %= 4;
    return x;
}

