// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Division by zero is legal for integer values.
//
// From section 5.9 of the GLSL 1.30 spec:
//     Dividing by zero does not cause an exception but does result in an
//     unspecified value.

#version 130

int
f() {
    int x = 1 / 0;
    return x;
}

