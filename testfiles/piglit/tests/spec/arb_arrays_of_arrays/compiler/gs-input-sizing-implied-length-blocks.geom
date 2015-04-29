// Section 4.3.8.1 (Input Layout Qualifiers) of the GLSL 1.50 spec says:
//
//   All geometry shader input unsized array declarations will be
//   sized by an earlier input layout qualifier, when present, as per
//   the following table.
//
// Followed by a table mapping each allowed input layout qualifier to
// the corresponding input length.
//
// This test verifies that if an unsized array declaration follows an
// input layout qualifier, the size is implied.  This test verifies
// the case for input interface blocks.
//
// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_arrays_of_arrays
// check_link: false
// [end config]

#version 150
#extension GL_ARB_arrays_of_arrays: enable

layout(lines) in;

in blk {
  vec4 Color;
} inst[][3];

uniform int foo[inst.length() == 2 ? 1 : -1];
uniform int foo2[inst[1].length() == 3 ? 1 : -1];
