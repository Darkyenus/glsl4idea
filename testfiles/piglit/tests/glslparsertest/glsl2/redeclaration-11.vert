// [config]
// expect_result: fail
// glsl_version: 1.20
//
// [end config]

/* FAIL - in 1.20, variables hide functions, so the call is illegal. */
#version 120

void foo(vec4 vs) {
   gl_Position = vs;
}

void main() {
   float foo = 1.0;
   foo(vec4(0.0));
}
