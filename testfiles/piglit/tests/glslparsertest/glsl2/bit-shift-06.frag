// [config]
// expect_result: pass
// glsl_version: 1.30
//
// [end config]

// Expected: PASS, glsl == 1.30
//
// Description: bit-shift with argument types:
//     - (ivecN, uvecN)
//     - (uvecN, ivecN)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "One operand can be signed while the other is unsigned."

#version 130
void main() {
    // ivecN = ivecN << uvecN -----------------

    // ivec2 = ivec2 << uvec2
    ivec2 v00 = ivec2(0, 1) << uvec2(0, 1);
    ivec2 v01 = ivec2(0, 1) >> uvec2(0, 1);

    // ivec3 = ivec3 << uvec3
    ivec3 v02 = ivec3(0, 1, 2) << uvec3(0, 1, 2);
    ivec3 v03 = ivec3(0, 1, 2) >> uvec3(0, 1, 2);

    // ivec4 = ivec4 << uvec4
    ivec4 v04 = ivec4(0, 1, 2, 3) << uvec4(0, 1, 2, 3);
    ivec4 v05 = ivec4(0, 1, 2, 3) >> uvec4(0, 1, 2, 3);

    // uvecN = uvecN << ivecN -----------------

    // uvec2 = uvec2 << ivec2
    uvec2 v06 = uvec2(0, 1) << ivec2(0, 1);
    uvec2 v07 = uvec2(0, 1) >> ivec2(0, 1);

    // uvec3 = uvec3 << ivec3
    uvec3 v08 = uvec3(0, 1, 2) << ivec3(0, 1, 2);
    uvec3 v09 = uvec3(0, 1, 2) >> ivec3(0, 1, 2);

    // uvec4 = uvec4 << ivec4
    uvec4 v10 = uvec4(0, 1, 2, 3) << ivec4(0, 1, 2, 3);
    uvec4 v11 = uvec4(0, 1, 2, 3) >> ivec4(0, 1, 2, 3);
}
