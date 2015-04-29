// [config]
// expect_result: pass
// glsl_version: 1.30
//
// [end config]

// Expected: PASS, glsl == 1.30
//
// Description: bit-logic ops with argument types:
//     - (ivecN, int)
//     - (uvecN, uint)
//     - (int, ivecN)
//     - (uint, uvecN)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "If one operand is a scalar and the other a vector, the scalar is applied
// component-wise to the vector, resulting in the same type as the vector."

#version 130
void main() {
    // (ivecN, int) --------------------------

    // (ivec2, int)
    ivec2 v00 = ivec2(0, 1) & int(7);
    ivec2 v01 = ivec2(0, 1) | int(7);

    // (ivec3, int)
    ivec3 v02 = ivec3(0, 1, 2) & int(7);
    ivec3 v03 = ivec3(0, 1, 2) | int(7);

    // (ivec4, int)
    ivec4 v04 = ivec4(0, 1, 2, 3) & int(7);
    ivec4 v05 = ivec4(0, 1, 2, 3) | int(7);

    // (uvecN, uint) --------------------------

    // (uvec2, uint)
    uvec2 v10 = uvec2(0, 1) & uint(7);
    uvec2 v11 = uvec2(0, 1) | uint(7);

    // (uve13, uint)
    uvec3 v12 = uvec3(0, 1, 2) & uint(7);
    uvec3 v13 = uvec3(0, 1, 2) | uint(7);

    // (uve14, uint)
    uvec4 v14 = uvec4(0, 1, 2, 3) & uint(7);
    uvec4 v15 = uvec4(0, 1, 2, 3) | uint(7);

    // (int, ivecN) --------------------------

    // (int, ivec2)
    ivec2 v20 = int(7) & ivec2(0, 1);
    ivec2 v21 = int(7) | ivec2(0, 1);

    // (int2 ivec3)
    ivec3 v22 = int(7) & ivec3(0, 1, 2);
    ivec3 v23 = int(7) | ivec3(0, 1, 2);

    // (int2 ivec4)
    ivec4 v24 =int(7) &  ivec4(0, 1, 2, 3);
    ivec4 v25 = int(7) | ivec4(0, 1, 2, 3);

    // (int, uvecN) --------------------------

    // (uint, uvec2)
    uvec2 v30 = uint(7) & uvec2(0, 1);
    uvec2 v31 = uint(7) | uvec2(0, 1);

    // (uin32 uvec3)
    uvec3 v32 = uint(7) & uvec3(0, 1, 2);
    uvec3 v33 = uint(7) | uvec3(0, 1, 2);

    // (uin32 uvec4)
    uvec4 v34 =uint(7) &  uvec4(0, 1, 2, 3);
    uvec4 v35 = uint(7) | uvec4(0, 1, 2, 3);
}
