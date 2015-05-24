// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]
//
// Tests for invalid input layout qualifiers.
//
// GLSLangSpec 1.50, section 4.3.8.1 (Input Layout Qualifiers):
// "The layout qualifier identifiers for geometry shader inputs are
//    layout-qualifier-id
//      points
//      lines
//      lines_adjacency
//      triangles
//      triangles_adjacency"
//

#version 150

layout(line_strip) in;

void main()
{
}
