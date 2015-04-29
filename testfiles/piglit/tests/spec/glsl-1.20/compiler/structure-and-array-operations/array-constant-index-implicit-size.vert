/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "If an array is indexed with an expression that is not an integral
 *     constant expression, or if an array is passed as an argument to a
 *     function, then its size must be declared before any such use."
 */
#version 120

/* Assume the array is sized in a different compilation unit.
 */
vec4 [] an_array;

void main()
{
  gl_Position = an_array[2];
}
