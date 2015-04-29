// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Section 4.3.8.1(Input Layout Qualifiers) of the GLSL 1.50 spec says:
// "Vertex shaders do not have any input layout qualifiers."

#version 150

layout(points) in vec4 b;

void main()
{
	gl_Position = b;
}
