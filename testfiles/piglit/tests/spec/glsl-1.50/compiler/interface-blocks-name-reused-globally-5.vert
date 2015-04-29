// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// GLSLangSpec.1.50.11, 4.3.7 Interface Blocks:
// "Block names have no other use within a shader beyond interface matching;
//  it is an error to use a block name at global scope for anything other than
//  as a block name."
//
// Tests that an interface block name may not be reused globally as anything
// other than as a block name.

#version 150

struct block {
    vec4 z;
};

out block {
    vec4 a;
} inst;

void main()
{
}

