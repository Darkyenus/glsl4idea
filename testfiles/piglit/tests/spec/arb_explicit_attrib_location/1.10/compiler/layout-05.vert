// [config]
// expect_result: fail
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// The 'layout' qualifier must come before 'in' or 'out'

#version 110
#extension GL_ARB_explicit_attrib_location: require
in layout(location = 0) vec4 vertex;

void main()
{
	gl_Position = vertex;
}
