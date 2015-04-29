// [config]
// expect_result: pass
// glsl_version: 1.20
// [end config]
//
// Division by zero is legal for integer values.
//
// From section 5.9 of the GLSL 1.20 spec:
//     Dividing by zero does not cause an exception but does result in an
//     unspecified value.

#version 120

int
f() {
    int x = 1 / 0;
    return x;
}

