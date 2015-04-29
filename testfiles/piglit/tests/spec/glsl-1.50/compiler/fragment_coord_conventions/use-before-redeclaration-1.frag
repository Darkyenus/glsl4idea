/* [config]
 * expect_result: fail
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
 * Tests using gl_FragCoord before the redeclaration.
 */

#version 150

vec4 p = gl_FragCoord;
layout(origin_upper_left, pixel_center_integer) in vec4 gl_FragCoord;

void main()
{
}
