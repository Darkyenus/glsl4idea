// [config]
// expect_result: fail
// glsl_version: 1.20
//
// [end config]

/* FAIL - noise functions are not allowed in constant expressions */
#version 120
void main() {
   const vec4 noise = noise4(0.5);
}
