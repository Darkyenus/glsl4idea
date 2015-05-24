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

const vec4[] a = vec4[](vec4(0), vec4(1));
const vec4[] b = vec4[](vec4(1), vec4(0));
uniform vec4 c[((true) ? a : b).length()];

void main()
{
  gl_Position = c[0];
}
