// [config]
// expect_result: pass
// glsl_version: 1.20
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]

#version 120
#extension GL_ARB_explicit_attrib_location: require
centroid in vec4 color;

void main()
{
	gl_FragColor = color;
}
