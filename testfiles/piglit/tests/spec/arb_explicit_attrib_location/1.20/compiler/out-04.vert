// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// From the GL_ARB_explicit_attrib_location spec:
//
//     "Vertex shaders cannot have output layout qualifiers."

#version 120
#extension GL_ARB_explicit_attrib_location: require
in vec4 vertex;
layout(location = 0) out vec4 color;

void main()
{
	gl_Position = vertex;
	color = vec4(1.0);
}
