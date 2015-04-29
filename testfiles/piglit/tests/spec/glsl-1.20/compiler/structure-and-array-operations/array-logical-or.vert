/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 35 (page 41 of the PDF) of the GLSL 1.20 spec:
 *
 *     "In total, only the following operators are allowed to operate on
 *     arrays and structures as whole entities:
 *
 *     field or method selector .
 *     equality                 == !=
 *     assignment               =
 *     indexing (arrays only)   []"
 */
#version 120

uniform vec4 a[2];
uniform vec4 b[2];

void main()
{
  gl_Position = vec4(a || b);
}
