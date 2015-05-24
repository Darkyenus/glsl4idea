// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Section 4.3.8 (Layout Qualifiers) of the GLSL 1.50 spec says:
// "The tokens in any layout-qualifier-id-list are identifiers, not keywords.
//  Generally, they can be listed in any order."
//
// Section 4.3.8.2(Output Layout Qualifiers) of the GLSL 1.50 spec says:
// "One declaration can declare either a primitive type (points, line_strip, or
//  triangle_strip), or max_vertices, or both."

#version 150

layout(triangles) in;
layout(max_vertices = 3, triangle_strip) out;

void main()
{
}
