/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 20 (page 26 of the PDF) of the GLSL 1.20 spec:
 *
 *     "There are no implicit array or structure conversions. For example, an
 *     array of int cannot be implicitly converted to an array of float."
 */
#version 120

uniform int a[2];

void main()
{
  float b[2];
  b = a;
  gl_Position = vec4(b[0], b[0], b[1], b[1]);
}
