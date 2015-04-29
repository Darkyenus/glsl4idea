// [config]
// expect_result: pass
// glsl_version: 1.30
//
// [end config]

// Expected: PASS, glsl == 1.30
//
// Description: bit-shift with argument type (int, int)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "the operands must be signed or unsigned integers or integer vectors. [...]
// In all cases, the resulting type will be the same type as the left
// operand."

#version 130
void main() {
    int x0 = 4 << 1;
    int x1 = 4 >> 1;
}
