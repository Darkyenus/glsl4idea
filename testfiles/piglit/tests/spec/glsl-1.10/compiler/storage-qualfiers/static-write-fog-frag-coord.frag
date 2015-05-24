// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// From section 4.3.6 of the GLSL 1.10 spec:
//     A fragment shader can not write to a varying variable.
//
// From section 7.6 of the GLSL 1.10 spec:
//     The following varying variables are available to read from in a fragment shader.
//         ...
//     varying float gl_FogFragCoord;

#version 110

void g() {
    gl_FogFragCoord = 0.0;
}
