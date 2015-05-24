// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Section 4.3.8.2(Output Layout Qualifiers) of the GLSL 1.50 spec says:
// "All geometry shader output layout declarations in a program must declare the
//  same layout and same value for max_vertices."

#version 150

layout(lines) in;
layout(line_strip, max_vertices=3) out;

in vec4 pos[];

layout(triangle_strip, max_vertices=3) out;

void main()
{
}
