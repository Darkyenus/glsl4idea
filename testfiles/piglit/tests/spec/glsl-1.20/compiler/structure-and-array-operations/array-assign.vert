/* [config]
 * expect_result: pass
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

void main()
{
  vec4 a[2];
  vec4 b[2];
  vec4 c[2];

  a = vec4[2](vec4(0.0), vec4(2.0));
  b = vec4[ ](vec4(0.5), vec4(2.0));
  c = a;

  gl_Position = a[0] + b[0] + c[1];
}
