// [config]
// expect_result: pass
// glsl_version: 1.30
//
// [end config]

// Expected: PASS, glsl == 1.30
//
// Description: bit-shift-assign with argument types:
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
    ivec2 v00 = ivec2(0, 1);
    v00 <<= 7;
    v00 >>= 7;

    // (ivec3, int)
    ivec3 v01 = ivec3(0, 1, 2);
    v01 <<= 7;
    v01 >>= 7;

    // (ivec4, int)
    ivec4 v02 = ivec4(0, 1, 2, 3);
    v02 <<= 7;
    v02 >>= 7;

    // (uvecN, int) --------------------------

    // (uvec2, int)
    uvec2 v10 = uvec2(0, 1);
    v10 <<= 7;
    v10 >>= 7;

    // (uvec3, int)
    uvec3 v11 = uvec3(0, 1, 2);
    v11 <<= 7;
    v11 >>= 7;

    // (uvec4, int)
    uvec4 v12 = uvec4(0, 1, 2, 3);
    v12 <<= 7;
    v12 >>= 7;

    // (ivecN, uint) --------------------------

    // (ivec2, uint)
    ivec2 v20 = ivec2(0, 1);
    v20 <<= uint(7);
    v20 >>= uint(7);

    // (ivec3, uint)
    ivec3 v21 = ivec3(0, 1, 2);
    v21 <<= uint(7);
    v21 >>= uint(7);

    // (ivec4, uint)
    ivec4 v22 = ivec4(0, 1, 2, 3);
    v22 <<= uint(7);
    v22 >>= uint(7);

    // (uvecN, uint) --------------------------

    // (uvec2, uint)
    uvec2 v30 = uvec2(0, 1);
    v30 <<= uint(7);
    v30 >>= uint(7);

    // (uvec3, uint)
    uvec3 v31 = uvec3(0, 1, 2);
    v31 <<= uint(7);
    v31 >>= uint(7);

    // (uvec4, uint)
    uvec4 v32 = uvec4(0, 1, 2, 3);
    v32 <<= uint(7);
    v32 >>= uint(7);
}
