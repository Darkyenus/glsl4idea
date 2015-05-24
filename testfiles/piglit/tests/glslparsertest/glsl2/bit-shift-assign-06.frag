// [config]
// expect_result: pass
// glsl_version: 1.30
//
// [end config]

// Expected: PASS, glsl == 1.30
//
// Description: bit-shift-assign with argument types:
//     - (ivecN, uvecN)
//     - (uvecN, ivecN)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "One operand can be signed while the other is unsigned."

#version 130
void main() {
    // (ivecN, uvecN) --------------------------

    ivec2 iv2 = ivec2(0, 1);
    iv2 <<= uvec2(0, 1);
    iv2 >>= uvec2(0, 1);

    ivec3 iv3 = ivec3(0, 1, 2);
    iv3 <<= uvec3(0, 1, 2);
    iv3 >>= uvec3(0, 1, 2);

    ivec4 iv4 = ivec4(0, 1, 2, 3);
    iv4 <<= uvec4(0, 1, 2, 3);
    iv4 >>= uvec4(0, 1, 2, 3);

    // (uvecN, ivecN) --------------------------

    uvec2 uv2 = uvec2(0, 1);
    uv2 <<= ivec2(0, 1);
    uv2 >>= ivec2(0, 1);

    uvec3 uv3 = uvec3(0, 1, 2);
    uv3 <<= ivec3(0, 1, 2);
    uv3 >>= ivec3(0, 1, 2);

    uvec4 uv4 = uvec4(0, 1, 2, 3);
    uv4 <<= ivec4(0, 1, 2, 3);
    uv4 >>= ivec4(0, 1, 2, 3);
}
