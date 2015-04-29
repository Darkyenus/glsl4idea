// [config]
// expect_result: fail
// glsl_version: 1.30
//
// [end config]

// Expected: FAIL, glsl == 1.30
//
// Description: bit-not with uvec2 argument
//
// From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
// "The operand must be of type signed or unsigned integer or integer vector,"

#version 130
void main() {
    vec2 v = ~ vec2(1, 2);
}
