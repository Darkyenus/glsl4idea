// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Tests that a pre-declared struct can be used an interface block.
//
// GLSLangSpec.1.50.11, 4.3.7 Interface Blocks:
// "built-in types, previously declared structures, and arrays of these
//  are allowed as the type of a declarator in the same manner they are
//  allowed outside a block."

#version 150

struct test_struct {
	int a;
	float b;
};

out block {
    test_struct c;
} inst;

void main()
{
}
