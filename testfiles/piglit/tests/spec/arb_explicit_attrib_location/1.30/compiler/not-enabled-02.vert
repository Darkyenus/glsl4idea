// [config]
// expect_result: fail
// glsl_version: 1.30
// require_extensions: GL_ARB_shading_language_420pack
// [end config]
//
// Try to use layout(location) without enabling the extension.

#version 130
#extension GL_ARB_shading_language_420pack: enable

layout(location = 0) in vec4 vertex;

void main()
{
	gl_Position = vertex;
}
