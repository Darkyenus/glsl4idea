// [config]
// expect_result: pass
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]

#version 110
#extension GL_ARB_explicit_attrib_location: require
layout(location = 0x01) in vec4 vertex;

void main()
{
	gl_Position = vertex;
}
