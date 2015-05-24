// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Tests that a struct cannot be defined within a block.
//
// GLSLangSpec.1.50.11, 4.3.7 Interface Blocks:
// "structure definitions cannot be nested inside a block"

#version 150

out block {
	struct test_struct {
		int a;
		float b;
	};
	test_struct c;
	float d;
} inst;

void main()
{
	inst.c.a = 1;
	inst.d = 1.0;
}
