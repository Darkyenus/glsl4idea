// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Section 4.3.8.1(Input Layout Qualifiers) of the GLSL 1.50 spec says:
// "Geometry shaders allow input layout qualifiers only on the interface
//  qualifier in, not on an input block, block member, or variable. The layout
//  qualifier identifiers for geometry shader inputs are
//	points
//	lines
//	lines_adjacency
//	triangles
//	triangles_adjacency
//  Only one argument is accepted."

#version 150

layout(points, triangles_adjacency) in;
layout(points, max_vertices = 1) out;

void main()
{
}
