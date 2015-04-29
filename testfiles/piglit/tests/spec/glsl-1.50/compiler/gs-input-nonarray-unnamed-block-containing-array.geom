// Section 4.3.4 (Inputs) of the GLSL 1.50 spec says:
//
//     Geometry shader input variables get the per-vertex values
//     written out by vertex shader output variables of the same
//     names. Since a geometry shader operates on a set of vertices,
//     each input varying variable (or input block, see interface
//     blocks below) needs to be declared as an array.
//
// This test verifies that trying to create an unnamed non-array input
// interface block produces an error, even if everything in the block
// is itself an array.
//
// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]

#version 150

in blk {
  vec4 foo[3];
};
