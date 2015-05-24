// [config]
// expect_result: pass
// glsl_version: 1.20
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]

#version 120
#extension GL_ARB_explicit_attrib_location: require
layout(location = 01) in vec4 vertex;

void main()
{
	gl_Position = vertex;
}
