// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]
//
// From the ARB_tessellation_shader spec (Section 4.3.8.2):
//
//  "It is a compile-time error if the output patch vertex count specified in
//  an output layout qualifier does not match the array size specified in any
//  output variable declaration in the same shader."

#version 150
#extension GL_ARB_tessellation_shader: require

layout(vertices = 3) out;

out vec4 four_not_equal_three[4];

/* Some compilers generate spurious errors if a shader does not contain
 * any code or declarations.
 */
int foo(void) { return 1; }

