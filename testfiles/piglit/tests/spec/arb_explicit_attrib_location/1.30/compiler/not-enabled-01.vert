// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Try to use layout(location) without enabling the extension.

#version 130

layout(location = 0) in vec4 vertex;

void main()
{
	gl_Position = vertex;
}
