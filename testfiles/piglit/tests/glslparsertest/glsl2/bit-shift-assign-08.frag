// [config]
// expect_result: fail
// glsl_version: 1.30
//
// [end config]

// Expected: FAIL, glsl == 1.30
//
// Description: bit-shift-assign with argument type (int, ivec2)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "If the first operand is a scalar, the second operand has to be a scalar as
// well."

#version 130
void main() {
    int x = 7;
    x <<= ivec2(0, 1);
}
