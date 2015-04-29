// [config]
// expect_result: fail
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// Even though there are too may outputs active, the spec says that a *link*
// error is generated.  The changes to section 3.9.2 say:
//
//     "Output binding assignments will cause LinkProgram to fail:
//
//           * if the number of active outputs is greater than the value of
//             MAX_DRAW_BUFFERS;
//
//           * if the program has an active output assigned to a location
//             greater than or equal to the value of
//             MAX_DUAL_SOURCE_DRAW_BUFFERS and has an active output assigned
//             an index greater than or equal to one;
//
//           * if more than one varying out variable is bound to the same
//             number and index; or
//
//           * if the explicit binding assigments do not leave enough space
//             for the linker to automatically assign a location for a varying
//             out array, which requires multiple contiguous locations."
//
// However, it is invalid to declare an output array in GLSL 1.10.

#version 110
#extension GL_ARB_explicit_attrib_location: require
layout(location = 0) out vec4 data[gl_MaxDrawBuffers + 1];

void main()
{
	for (int i = 0; i < gl_MaxDrawBuffers + 1; i++)
		data = vec4(i);
}
