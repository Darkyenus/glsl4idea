// [config]
// expect_result: pass
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// "out" is allowed in shader output declarations in GLSL 1.10 when
// GL_ARB_explicit_attrib_location is enabled

#version 110
#extension GL_ARB_explicit_attrib_location: require
in vec4 vertex;
out vec4 color;

void main()
{
	gl_Position = vertex;
	color = vec4(1.0);
}
