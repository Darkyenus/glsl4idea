// [config]
// expect_result: fail
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// The 'location' layout qualifier can only be used with 'in' and 'out'.

#version 110
#extension GL_ARB_explicit_attrib_location: require
layout(location = 0) attribute vec4 vertex;

void main()
{
	gl_Position = vertex;
}
