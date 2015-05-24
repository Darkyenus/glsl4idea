/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * check_link: true
 * [end config]
 *
 * Although it is not explicitly stated in the GLSL spec, in all
 * examples, redeclarations of built-in variables (such as
 * gl_ClipDistance) preserve the in/out qualifiers.
 *
 * This test verifies that a shader is rejected if it tries to
 * redeclare gl_ClipDistance without an in/out qualifier.
 */
#version 130

float gl_ClipDistance[3];

void main()
{
  gl_FragColor = vec4(0.0);
}
