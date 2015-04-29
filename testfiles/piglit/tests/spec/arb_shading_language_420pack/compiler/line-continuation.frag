/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

/*
 * The ARB_shading_language_420pack says:
 * 
 *    "Including the following line in a shader will enable module import and
 *     related extended language features described in this extension:
 *
 *             #extension GL_ARB_shading_language_420pack : <behavior>
 *
 *     where <behavior> is as specified in section 3.3 for the #extension
 *     directive."
 *
 * As a result, strictly speaking we need to enable the extension before line
 * continuation is enabled.
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable

/* Make sure it works in comments too.*\
/

uniform vec4 f\
oo;

void main() {
	gl_FragColor = foo;
}
