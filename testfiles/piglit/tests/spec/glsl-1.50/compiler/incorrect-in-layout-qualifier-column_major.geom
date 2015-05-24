// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]
//
// Tests for invalid input layout qualifiers.
//
// GLSLangSpec 1.50, section 4.3.8.1 (Input Layout Qualifiers):
// "Only one argument is accepted. For example,
//  layout(triangles) in;"
//

#version 150

layout(points, column_major) in;

void main()
{
}
