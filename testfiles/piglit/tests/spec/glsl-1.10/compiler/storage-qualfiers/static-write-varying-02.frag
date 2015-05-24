// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// From section 4.3.6 of the GLSL 1.10 spec:
//     A fragment shader can not write to a varying variable.

#version 110

varying float x;

void f(out float y) {
    y = 0.0;
}

void g() {
    f(x);
}
