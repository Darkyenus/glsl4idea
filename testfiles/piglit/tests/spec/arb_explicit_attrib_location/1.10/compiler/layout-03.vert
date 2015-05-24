// [config]
// expect_result: pass
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// 'location' must be an integer constant

#version 110
#extension GL_ARB_explicit_attrib_location: require
#define X 1
layout(location = X) in vec4 vertex;

void main()
{
	gl_Position = vertex;
}
