// [config]
// expect_result: fail
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// The 'location' layout qualifier can only be used with 'in' and 'out'.  Also,
// from the GL_ARB_explicit_attrib_location spec:
//
//     "Vertex shaders cannot have output layout qualifiers."

#version 110
#extension GL_ARB_explicit_attrib_location: require
attribute vec4 vertex;
layout(location = 0) varying vec4 color;

void main()
{
	gl_Position = vertex;
	color = vec4(1.0);
}
