// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// The 'layout' qualifier must come before 'in' or 'out'

#version 120
#extension GL_ARB_explicit_attrib_location: require
out layout(location = 0) vec4 color;

void main()
{
	color = vec4(1.0);
}
