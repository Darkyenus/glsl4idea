/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * From page 35 (page 41 of the PDF) of the GLSL 1.20 spec:
 *
 *     "The equality operators and assignment operator are only allowed if the
 *     two operands are same size and type....Both array operands must be
 *     explicitly sized. When using the equality operators ... two arrays are
 *     equal if and only if all the elements are element-wise equal.
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

uniform vec4 a[2][3];

void main()
{
  vec4 b[][3];
  vec4[3] c[]

  // Implicitly size b to match a.
  b[0][2] = vec4(1.0);
  b[1][2] = vec4(1.0);

  // Implicitly size c to match a.
  c[0][2] = vec4(1.0);
  c[1][2] = vec4(1.0);

  gl_Position = vec4(float(a == b), float(b == c), 0, 1);
}
