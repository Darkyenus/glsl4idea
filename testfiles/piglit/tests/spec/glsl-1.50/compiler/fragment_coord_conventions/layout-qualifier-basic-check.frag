/* [config]
 * expect_result: pass
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
 * This test is just a check to make sure the arb_fragment_coord_conventions
 * extension is switched on in core compiler.
 *
 */

#version 150

layout(origin_upper_left, pixel_center_integer) in vec4 gl_FragCoord;

void main()
{
     gl_FragColor = gl_FragCoord.xyzz;
}
