// [config]
// expect_result: pass
// glsl_version: 1.30
//
// [end config]

// Expected: PASS, glsl == 1.30
//
// Description: bit-logic ops with argument type (ivecN, ivecN)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "The operands must be of type signed or unsigned integers or integer
// vectors."

#version 130
void main() {
    // ivec2
    ivec2 v00 = ivec2(0, 1) & ivec2(0, 1);
    ivec2 v01 = ivec2(0, 1) & ivec2(0, 1);

    // ivec3
    ivec3 v02 = ivec3(0, 1, 2) | ivec3(0, 1, 2);
    ivec3 v03 = ivec3(0, 1, 2) | ivec3(0, 1, 2);

    // ivec4
    ivec4 v04 = ivec4(0, 1, 2, 3) ^ ivec4(0, 1, 2, 3);
    ivec4 v05 = ivec4(0, 1, 2, 3) ^ ivec4(0, 1, 2, 3);
}
