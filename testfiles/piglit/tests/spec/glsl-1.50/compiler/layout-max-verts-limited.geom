// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Section 4.3.8 (Output Layout Qualifiers) of the GLSL 1.50 spec says:
// "It is an error for the maximum number of vertices to be greater than
//  gl_MaxGeometryOutputVertices."
//
// Unfortunately, we can't easlily try to set max_vertices to
// gl_MaxGeometryOutputVertices+1, since "max_vertices=" must be
// followed by an integer-constant (not a constant expression), so as
// a stop gap, we just verify that setting max_vertices = INT_MAX
// leads to an error.

#version 150

layout(max_vertices = 2147483647) out;

void main()
{
}
