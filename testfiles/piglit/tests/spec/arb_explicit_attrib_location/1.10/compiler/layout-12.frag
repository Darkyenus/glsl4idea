// [config]
// expect_result: fail
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// While the GL_ARB_explicit_attrib_location specification does not say
// anything about generating errors for invalid indices, the GLSL 4.30
// spec clarifies this: "It is also a compile-time error if a fragment shader
// sets a layout index to less than 0 or greater than 1."  This matches the
// behavior of the equivalent API call, glBindFragDataLocationIndexed, which
// generates an INVALID_VALUE error if <index> is not 0 or 1.

#version 110
#extension GL_ARB_explicit_attrib_location: require
layout(location = 0, index = 2) out vec4 color;

void main()
{
	color = vec4(1.0);
}
