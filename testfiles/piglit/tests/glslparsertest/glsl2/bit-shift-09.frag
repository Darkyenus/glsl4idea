// [config]
// expect_result: fail
// glsl_version: 1.30
//
// [end config]

// Expected: FAIL, glsl <= 1.30
//
// Description: bit-shift with arguments (mat4, int)
//
// From page 50 (page 56 of the PDF) of the GLSL 1.30 spec:
// "the operands must be signed or unsigned integers or integer vectors."

#version 130
void main() {
    bool b = true << 0;
}

