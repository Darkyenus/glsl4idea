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
// input layout qualifier, the size is implied.
//
// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: false
// [end config]

#version 150

layout(lines) in;
in vec4 Color[];

uniform int foo[Color.length() == 2 ? 1 : -1];
