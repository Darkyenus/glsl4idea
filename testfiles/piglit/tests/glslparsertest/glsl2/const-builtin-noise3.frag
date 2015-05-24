// [config]
// expect_result: fail
// glsl_version: 1.20
//
// [end config]

/* FAIL - noise functions are not allowed in constant expressions */
#version 120
void main() {
   const vec3 noise = noise3(0.5);
}
