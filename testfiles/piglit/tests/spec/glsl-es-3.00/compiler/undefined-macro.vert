#version 300 es

/* [config]
 * expect_result: fail
 * glsl_version: 3.00
 * [end config]
 *
 * Page 11 (page 17 of the PDF) of the OpenGL ES Shading Language 3.00 spec
 * says:
 *
 *     "Undefined identifiers not consumed by the defined operator do not
 *     default to '0'. Use of such identifiers causes an error."
 */

#if FOO
#endif

void main()
{
  gl_Position = vec4(0.);
}
