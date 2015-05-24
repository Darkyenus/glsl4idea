/* [config]
 * expect_result: fail
 * glsl_version: 1.50
 * check_link: false
 * [end config]
 */

/* Section 4.3.8.1 (Input Layout Qualifiers) of the GLSL 1.50 spec says:
 *
 *     "Fragment shaders can have an input layout only for redeclaring the
 *     built-in variable gl_FragCoord (see section 7.2 Fragment Shader
 *     Special Variables). The layout qualifier identifiers for
 *     gl_FragCoord are
 *
 *     layout-qualifier-id:
 *         origin_upper_left
 *         pixel_center_integer"
 *
 *     "If gl_FragCoord is redeclared in any fragment shader in a program,
 *      it must be redeclared in all the fragment shaders in that program
 *      that have a static use gl_FragCoord. All redeclarations of
 *      gl_FragCoord in all fragment shaders in a single program must have
 *      the same set of qualifiers."
 *
 * Tests the conflicting redeclarations in a fragment shader cause the compile
 * failure even if gl_FragCoord is not used.
 */

#version 150

layout(origin_upper_left, pixel_center_integer) in vec4 gl_FragCoord;
layout(origin_upper_left) in vec4 gl_FragCoord;
out vec4 fragcolor;
void main()
{
     fragcolor = vec4(1.0);
}
