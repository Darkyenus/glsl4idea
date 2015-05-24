// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Tests that a uniform block array must specify an array size.
//
// GLSLangSpec.1.50.11, 4.3.7 Interface Blocks:
// "As the array size indicates the number of buffer objects needed,
//  uniform block array declarations must specify an array size."

#version 150

uniform Block {
    float a;
	vec3 b;
} array_blocks[];

void main()
{
}
