// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Tests that the attribute qualifier is rejected in an interface block.
//
// GLSLangSpec.1.50.11, 4.3.7 Interface Blocks:
// "Declarations using the deprecated attribute and varying qualifiers
//  are not allowed."

#version 150

out block {
    attribute vec4 a; // illegal: can't use deprecated attribute keyword
} inst;

void main()
{
}

