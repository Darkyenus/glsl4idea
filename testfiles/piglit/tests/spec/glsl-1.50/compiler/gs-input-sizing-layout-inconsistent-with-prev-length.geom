// Section 4.3.8.1 (Input Layout Qualifiers) of the GLSL 1.50 spec says:
//
//   It is a compile-time error if a layout declaration's array size
//   (from table above) does not match any array size specified in
//   declarations of an input variable in the same shader.
//
// This test verifies the case where the input variable declaration
// precedes the layout declaration.
//
// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]

#version 150

in vec4 Color[3];
layout(lines) in;
