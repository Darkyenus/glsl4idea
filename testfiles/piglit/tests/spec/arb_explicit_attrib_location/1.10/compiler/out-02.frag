// [config]
// expect_result: fail
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// "out" is not allowed on function local variables in GLSL 1.10 even when
// GL_ARB_explicit_attrib_location is enabled

#version 110
#extension GL_ARB_explicit_attrib_location: require
out vec4 color;

void main()
{
	out vec4 foo;

	color = vec4(1.0);
}
