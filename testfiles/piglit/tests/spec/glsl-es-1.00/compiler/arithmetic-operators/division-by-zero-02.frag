// [config]
// expect_result: pass
// glsl_version: 1.00
// [end config]
//
// Division by zero is legal for integer values.
//
// From section 5.9 of the GLSL ES 1.00 spec:
//     Dividing by zero does not cause an exception but does result in an
//     unspecified value.

#version 100

int
f() {
    int x = 1 / 0;
    return x;
}
