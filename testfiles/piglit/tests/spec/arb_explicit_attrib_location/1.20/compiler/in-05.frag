// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// 'centroid' must appear before 'in'.

#version 120
#extension GL_ARB_explicit_attrib_location: require
in centroid vec4 color;

void main()
{
	gl_FragColor = color;
}
