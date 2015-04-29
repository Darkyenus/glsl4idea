// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]
//
// Tests that output layout qualifiers cannot be used on a variable declaration.
//
// GLSLangSpec 1.50, section 4.3.8.2 (Output Layout Qualifiers):
// "Geometry shaders can have output layout qualifiers only on the interface
//  qualifier out, not on an output block or variable declaration."

#version 150

layout(points) out float c;

void main()
{
}
