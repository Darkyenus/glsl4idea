// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Tests that sampler types are not allowed within a block.
//
// GLSLangSpec.1.50.11, 4.3.7 Interface Blocks:
// "Types and declarators are the same as for other input, output, and uniform
//  variable declarations outside blocks, with these exceptions:
//	• initializers are not allowed
//	• sampler types are not allowed
//	• structure definitions cannot be nested inside a block"

#version 150

out block {
	sampler2DShadow a;
} inst;

void main()
{
}
