#version 300 es

/* [config]
 * expect_result: pass
 * glsl_version: 3.00
 * [end config]
 *
 * Page 8 (page 14 of the PDF) of the OpenGL ES Shading Language 3.00 spec
 * says:
 *
 *     "Backslash ('\'), used to indicate line continuation when immediately
 *     preceding a new-line."
 */

#define some_macro \
  junk \
  junk


void main()
{
  /* Nothing says that line continuation can only be used in a macro
   * definition, even though that's the only sensible place to use it.
   */
  gl_Position = \
    vec4(0.);
}
