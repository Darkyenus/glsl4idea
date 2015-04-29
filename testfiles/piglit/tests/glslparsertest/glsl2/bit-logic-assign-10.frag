// [config]
// expect_result: pass
// glsl_version: 1.30
//
// [end config]

// Expected: PASS, glsl == 1.30
//
// Description: bit-logic assignment ops with argument types:
//     - (ivecN, int)
//     - (uvecN, uint)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "If one operand is a scalar and the other a vector, the scalar is applied
// component-wise to the vector, resulting in the same type as the vector."

#version 130
void main() {
    // (ivecN, int) --------------------------

    ivec2 v00 = ivec2(0, 1);
    v00 &= int(7);

    ivec3 v01 = ivec3(0, 1, 2);
    v01 |= int(7);

    ivec4 v02 = ivec4(0, 1, 2, 3);
    v02 ^= int(7);

    // (uvecN, uint) --------------------------

    uvec2 v10 = uvec2(0, 1);
    v10 &= uint(7);

    uvec3 v11 = uvec3(0, 1, 2);
    v11 |= uint(7);

    uvec4 v12 = uvec4(0, 1, 2, 3);
    v12 ^= uint(7);
}
