// [config]
// expect_result: fail
// glsl_version: 1.30
//
// [end config]

// Expected: FAIL, glsl == 1.30
//
// Description: bit-or assignment with argument type (int, ivec2)

#version 130
void main() {
    int x = 0;
    x |= ivec2(0, 1);
}
