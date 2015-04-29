// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Section 4.3.8(Layout Qualifiers) of the GLSL 1.50 spec says:
// "identifiers are not case sensitive, unless explicitly noted
//  otherwise."

#version 150

layout(TriAngleS) in;
layout(triangle_strip, max_vertices = 3) out;

void main()
{
}
