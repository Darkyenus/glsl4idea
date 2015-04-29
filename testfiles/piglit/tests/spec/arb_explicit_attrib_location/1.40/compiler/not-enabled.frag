// [config]
// expect_result: fail
// glsl_version: 1.40
// [end config]
//
// Try to use layout(location) without enabling the extension.

#version 140

layout(location = 0) out vec4 c;

void main()
{
	c = vec4(0);
}
