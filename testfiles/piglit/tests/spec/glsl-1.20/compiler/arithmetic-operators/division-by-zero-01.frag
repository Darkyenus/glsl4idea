// [config]
// expect_result: pass
// glsl_version: 1.20
// [end config]
//
// Division by zero is legal for floating point values.
//
// From section 5.9 of the GLSL 1.20 spec:
//     Dividing by zero does not cause an exception but does result in an
//     unspecified value.

#version 120

float
f() {
    float x = 1.0 / 0.0;
    return x;
}

