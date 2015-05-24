/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * [end config]
 *
 * From page 38 (page 44 of the PDF) of the GLSL 1.20 spec:
 *
 *     "The ternary selection operator (?:). It operates on three expressions
 *     (exp1 ? exp2 : exp3)....The second and third expressions can be any
 *     type, as long their types match....This resulting matching type is
 *     the type of the entire expression."
 */
#version 120

uniform vec4 a[2];
uniform vec4 b[2];
uniform int i;
uniform bool pick_from_a_or_b;

void main()
{
  gl_Position = (pick_from_a_or_b ? a : b)[i];
}
