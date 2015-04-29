// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Tests that an initializer is not allowed within a block.
//
// GLSLangSpec.1.50.11, 4.3.7 Interface Blocks:
// "Types and declarators are the same as for other input, output, and uniform
//  variable declarations outside blocks, with these exceptions:
//	• initializers are not allowed
//	• sampler types are not allowed
//	• structure definitions cannot be nested inside a block"

#version 150

out block {
	vec4 a(1., 0., 0., 1.);
} inst;

void main()
{
	inst.a = vec4(0.);
}
