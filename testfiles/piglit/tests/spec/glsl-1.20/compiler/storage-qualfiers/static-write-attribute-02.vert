// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//
// From section 4.3.4 of the GLSL 1.20 spec:
//     Attribute variables are read-only as far as the vertex shader is
//     concerned.

#version 120

attribute float x;

void f(out float y) {
    y = 0.0;
}

void g() {
    f(x);
}
