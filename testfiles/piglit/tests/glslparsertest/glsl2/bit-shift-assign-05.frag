// [config]
// expect_result: pass
// glsl_version: 1.30
//
// [end config]

// Expected: PASS, glsl == 1.30
//
// Description: bit-shift-assign with argument types:
//     - (int, uint)
//     - (uint, int)
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "One operand can be signed while the other is unsigned."

#version 130
void main() {
    // int <<= uint
    int x0 = 4;
    x0 <<= uint(1);

    // uint >>= int
    uint x1 = uint(4);
    x1 >>= int(1);
}
