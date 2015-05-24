// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Section 4.3.8.2(Output Layout Qualifiers) of the GLSL 1.50 spec says:
// "Vertex and fragment shaders cannot have output layout qualifiers."

#version 150

layout(points) out vec4 b;

void main()
{
	gl_FragColor = vec4(0., 1., 0., 1.);
}
