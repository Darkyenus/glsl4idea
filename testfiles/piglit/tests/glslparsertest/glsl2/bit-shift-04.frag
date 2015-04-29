// [config]
// expect_result: pass
// glsl_version: 1.30
//
// [end config]

// Expected: PASS, glsl == 1.30
//
// Description: bit-shift with argument type (uvecN, uvecN)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "the operands must be signed or unsigned integers or integer vectors. [...]
// In all cases, the resulting type will be the same type as the left
// operand."

#version 130
void main() {
    // uvec2
    uvec2 v00 = uvec2(0, 1) << uvec2(0, 1);
    uvec2 v01 = uvec2(0, 1) >> uvec2(0, 1);

    // uvec3
    uvec3 v02 = uvec3(0, 1, 2) << uvec3(0, 1, 2);
    uvec3 v03 = uvec3(0, 1, 2) >> uvec3(0, 1, 2);

    // uvec4
    uvec4 v04 = uvec4(0, 1, 2, 3) << uvec4(0, 1, 2, 3);
    uvec4 v05 = uvec4(0, 1, 2, 3) >> uvec4(0, 1, 2, 3);
}
