// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// 'centroid' must appear before 'out'.

#version 120
#extension GL_ARB_explicit_attrib_location: require
in vec4 vertex;
out centroid vec4 color;

void main()
{
	gl_Position = vertex;
	color = vec4(1.0);
}
