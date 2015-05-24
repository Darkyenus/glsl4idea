// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - calls to builtins are not legal in 1.10 */
#version 110
void main() {
   const float one = abs(-1.0);
}
