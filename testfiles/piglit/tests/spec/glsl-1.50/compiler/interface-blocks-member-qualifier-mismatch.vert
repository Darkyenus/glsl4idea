// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Tests that member qualifier matches the interface block type.
//
// GLSLangSpec.1.50.11, 4.3.7 Interface Blocks:
// "Input variables, output variables, and uniform variables can only
//  be in in blocks, out blocks, and uniform blocks, respectively."

#version 150

out block {
    uniform vec4 a; // illegal: uniform qualifier within out block
} inst;

void main()
{
}

