// The config section below is required.
//
// [config]
// # The config section may contain comments.
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Description: bit-logic assignment ops with argument type (int, int)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
//     "The operands must be of type signed or unsigned integers or integer
//     vectors."

#version 130
void main() {
    int x = 0;
    x &= 1;
    x |= 1;
    x ^= 1;
}
