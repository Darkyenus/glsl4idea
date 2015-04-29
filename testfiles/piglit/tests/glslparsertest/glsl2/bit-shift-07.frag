// [config]
// expect_result: pass
// glsl_version: 1.30
//
// [end config]

// Expected: PASS, glsl == 1.30
//
// Description: bit-shift with argument types:
//     - (ivecN, int)
//     - (uvecN, int)
//     - (ivecN, uint)
//     - (uvecN, uint)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "One operand can be signed while the other is unsigned. [...] If the first
// operand is a vector, the second operand must be a scalar or a vector,"


#version 130
void main() {
    // (ivecN, int) --------------------------

    // (ivec2, int)
    ivec2 v00 = ivec2(0, 1) << 7;
    ivec2 v01 = ivec2(0, 1) >> 7;

    // (ivec3, int)
    ivec3 v02 = ivec3(0, 1, 2) << 7;
    ivec3 v03 = ivec3(0, 1, 2) >> 7;

    // (ivec4, int)
    ivec4 v04 = ivec4(0, 1, 2, 3) << 7;
    ivec4 v05 = ivec4(0, 1, 2, 3) >> 7;

    // (uvecN, int) --------------------------

    // (uvec2, int)
    uvec2 v10 = uvec2(0, 1) << 7;
    uvec2 v11 = uvec2(0, 1) >> 7;

    // (uvec3, int)
    uvec3 v12 = uvec3(0, 1, 2) << 7;
    uvec3 v13 = uvec3(0, 1, 2) >> 7;

    // (uvec4, int)
    uvec4 v14 = uvec4(0, 1, 2, 3) << 7;
    uvec4 v15 = uvec4(0, 1, 2, 3) >> 7;

    // (ivecN, uint) --------------------------

    // (ivec2, uint)
    ivec2 v20 = ivec2(0, 1) << uint(7);
    ivec2 v21 = ivec2(0, 1) >> uint(7);

    // (ivec3, uint)
    ivec3 v22 = ivec3(0, 1, 2) << uint(7);
    ivec3 v23 = ivec3(0, 1, 2) >> uint(7);

    // (ivec4, uint)
    ivec4 v24 = ivec4(0, 1, 2, 3) << uint(7);
    ivec4 v25 = ivec4(0, 1, 2, 3) >> uint(7);

    // (uvecN, uint) --------------------------

    // (uvec2, uint)
    uvec2 v30 = uvec2(0, 1) << uint(7);
    uvec2 v31 = uvec2(0, 1) >> uint(7);

    // (uvec3, uint)
    uvec3 v32 = uvec3(0, 1, 2) << uint(7);
    uvec3 v33 = uvec3(0, 1, 2) >> uint(7);

    // (uvec4, uint)
    uvec4 v34 = uvec4(0, 1, 2, 3) << uint(7);
    uvec4 v35 = uvec4(0, 1, 2, 3) >> uint(7);
}
