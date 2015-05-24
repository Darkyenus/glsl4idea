// [config]
// expect_result: fail
// glsl_version: 1.30
//
// [end config]

// Expected: FAIL, glsl == 1.30
//
// Description: bit-and with argument type (ivec2, uvec2)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "The operands cannot be vectors of differing size."

#version 130
void main() {
    ivec2 v = ivec2(1, 2) & ivec3(1, 2, 3);
}
