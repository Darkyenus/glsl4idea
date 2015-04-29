// [config]
// expect_result: fail
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// GL_ARB_explicit_attrib_location interacts with the 'centroid' qualifier,
// but it does not make it available in 1.10 shaders.

#version 110
#extension GL_ARB_explicit_attrib_location: require
centroid in vec4 color;

void main()
{
	gl_FragColor = color;
}
