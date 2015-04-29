// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// test that layout with just an index specified is rejected

#version 120
#extension GL_ARB_explicit_attrib_location: require
layout(index = 0) out vec4 color;

void main()
{
	color = vec4(1.0);
}
