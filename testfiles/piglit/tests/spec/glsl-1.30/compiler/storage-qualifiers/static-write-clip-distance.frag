// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// From section 4.3.4 of the GLSL 1.30 spec:
//     Variables declared as in or centroid in may not be written
//     to during shader execution.
//
// From section 7.2 of the GLSL 1.30 spec:
//     The built-in special variables that are accessible from a
//     fragment shader are intrinsically declared as follows:
//         ...
//     in float gl_ClipDistance[];

#version 130

void g() {
    gl_ClipDistance[0] = 0.0;
}
