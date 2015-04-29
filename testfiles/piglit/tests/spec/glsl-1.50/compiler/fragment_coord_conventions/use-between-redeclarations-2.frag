/* [config]
 * expect_result: pass
 * glsl_version: 1.50
 * check_link: false
 * [end config]
 */

/* Section 4.3.8.1 (Input Layout Qualifiers) of the GLSL 1.50 spec says:
 *
 *
 *    "Within any shader, the first redeclarations of gl_FragCoord must appear
 *     before any use of gl_FragCoord."
 *
 * Tests using gl_FragCoord between two redeclarations of gl_FragCoord with
 * no layout qualifiers.
 */

#version 150

in vec4 gl_FragCoord;
vec4 p = gl_FragCoord;
in vec4 gl_FragCoord;

void main()
{
}
