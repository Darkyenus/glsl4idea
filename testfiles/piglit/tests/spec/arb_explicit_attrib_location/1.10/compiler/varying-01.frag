// [config]
// expect_result: fail
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// The 'location' layout qualifier can only be used with 'in' and 'out'.  Also,
// from the GL_ARB_explicit_attrib_location spec:
//
//     "Vertex shaders allow input layout qualifiers on input variable
//     declarations."
//
// Just as output layouts are not allowed in vertex shaders, input
// layouts are not allowed in fragment shaders.

#version 110
#extension GL_ARB_explicit_attrib_location: require
layout(location = 0) varying vec4 color;

void main()
{
	gl_FragColor = color;
}
