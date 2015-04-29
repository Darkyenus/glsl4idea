// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Tests that in order to access a block member in a block array, the array
// index must be included.
//
// GLSLangSpec.1.50.11, 4.3.7 Interface Blocks:
// "For blocks declared as arrays, the array index must also be included
//  when accessing members"

#version 150

out Block {
	float a;
	vec3 b;
} array_blocks[2];

void main()
{
	array_blocks.a = 5.0;

}
