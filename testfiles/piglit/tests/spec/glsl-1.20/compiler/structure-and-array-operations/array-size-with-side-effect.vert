/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "When an array size is specified in a declaration, it must be an
 *     integral constant expression (see Section 4.3.3 "Constant Expressions")
 *     greater than zero."
 */
#version 120

void main()
{
  int x;
  vec4[(x = 3)] a;
  gl_Position = vec4(0.0);
}
