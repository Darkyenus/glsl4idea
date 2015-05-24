// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// Compilation should fail becuase the type 'B' is unknown.
//
// Reproduces Mesa bugzilla #33313.

B x[1];

void main() { gl_Position = vec4(1.0); }
