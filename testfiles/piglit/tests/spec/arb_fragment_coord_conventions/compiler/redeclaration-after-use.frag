/* [config]
 * expect_result: fail
 * glsl_version: 1.10
 * require_extensions: GL_ARB_fragment_coord_conventions
 * check_link: false
 * [end config]
 */

/*
 * Section 4.3.x.1 (Input Layout Qualifiers) of the
 * ARB_fragment_coord_conventions spec says:
 *
 *     "Within any shader, the first redeclarations of gl_FragCoord must appear
 *     before any use of gl_FragCoord."
 */

#version 110

#extension GL_ARB_fragment_coord_conventions : require

vec2 a = gl_FragCoord.xy;

in vec4 gl_FragCoord; //redeclaration after use should be illegal

void main()
{
	gl_FragColor = gl_FragCoord.xyzz;
}
