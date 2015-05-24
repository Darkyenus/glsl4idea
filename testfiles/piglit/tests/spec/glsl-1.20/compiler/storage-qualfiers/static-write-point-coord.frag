// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//
// From section 4.3.6 of the GLSL 1.20 spec:
//     A fragment shader can not write to a varying variable.
//
// From section 7.6 of the GLSL 1.20 spec:
//     The following varying variables are available to read from in a fragment shader.
//         ...
//     varying vec2 gl_PointCoord;

#version 120

void g() {
    gl_PointCoord = vec2(0.0);
}
