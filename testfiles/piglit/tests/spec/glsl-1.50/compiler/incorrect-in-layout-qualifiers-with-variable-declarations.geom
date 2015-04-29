// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]
//
// Tests that input layout qualifiers cannot be used on a variable declaration.
//
// GLSLangSpec 1.50, section 4.3.8.2 (Output Layout Qualifiers):
// "Geometry shaders allow input layout qualifiers only on the interface
//  qualifier in, not on an input block,block member, or variable."

#version 150

layout(points) in float c[];

void main()
{
}
