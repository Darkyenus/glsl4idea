// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// From section 4.3.4 of the GLSL 1.10 spec:
//     Attribute variables are read-only as far as the vertex shader is
//     concerned.
//
// From section 7.3 of the GLSL 1.10 spec:
//     The following attribute names are built into the OpenGL vertex
//     language and can be used from within a vertex shader to access
//     the current values of attributes declared by OpenGL.
//         ...
//     attribute vec4 gl_MultiTexCoord4;


#version 110

void f() {
	gl_MultiTexCoord4 = vec4(0.0);
}
