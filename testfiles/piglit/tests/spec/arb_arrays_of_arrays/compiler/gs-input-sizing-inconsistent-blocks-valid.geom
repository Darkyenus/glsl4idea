// This test verifies that declaring
// two geometry shader inputs with different array sizes only causes a
// compile error when the outer most dimension differs.
//
// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_arrays_of_arrays
// check_link: false
// [end config]

#version 150
#extension GL_ARB_arrays_of_arrays: enable

in blk1 {
  vec4 Color;
} inst2[2][4];

in blk2 {
  vec4 Color;
} inst3[2][5];
