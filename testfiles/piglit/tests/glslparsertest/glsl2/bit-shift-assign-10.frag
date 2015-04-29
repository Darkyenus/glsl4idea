// [config]
// expect_result: fail
// glsl_version: 1.30
//
// [end config]

// Expected: FAIL, glsl == 1.30
//
// Description: bit-shift-assign with unequally sized vectors
//
// See page 50 (page 56 of the PDF) of the GLSL 1.30 spec.

#version 130
void main() {
    ivec2 v = ivec2(0, 1);
    v <<= ivec3(0, 1, 2);
}
