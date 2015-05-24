// [config]
// expect_result: pass
// glsl_version: 1.30
// require_extensions: GL_ARB_explicit_attrib_location GL_ARB_explicit_uniform_location
// [end config]

#version 130
#extension GL_ARB_explicit_attrib_location: require
#extension GL_ARB_explicit_uniform_location: require
vec4 vertex;
layout(location = 41) uniform float foo;

void main()
{
	gl_Position = vertex;
}
