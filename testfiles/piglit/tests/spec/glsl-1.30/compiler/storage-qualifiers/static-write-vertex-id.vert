// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// From section 4.3.4 of the GLSL 1.30 spec:
//     Variables declared as in or centroid in may not be written
//     to during shader execution.
//
// From section 7.1 of the GLSL 1.30 spec:
//     These built-in vertex shader variables for communicating with
//     fixed functionality are intrinsically declared as follows:
//         ...
//     in int gl_VertexID;

#version 130

void g() {
    gl_VertexID = 0;
}
