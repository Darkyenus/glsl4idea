/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 23 (page 29 of the PDF) of the GLSL 1.20 spec:
 *
 *     "Attribute variables cannot be declared as arrays or structures."
 */
#version 120

attribute vec4 a[2]
uniform int i;

void main()
{
  gl_Position = a[i];
}
