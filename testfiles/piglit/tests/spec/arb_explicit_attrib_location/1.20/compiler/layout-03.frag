// [config]
// expect_result: pass
// glsl_version: 1.20
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// 'location' must be an integer constant

#version 120
#extension GL_ARB_explicit_attrib_location: require
#define X 1
layout(location = X) out vec4 color;

void main()
{
	color = vec4(1.0);
}
