/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

/*
 * The ARB_shading_language_420pack says:
 *
 *    "* Change from ASCII to UTF-8 for the language character set and also
 *       allow any characters inside comments."
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable

/* Testing UTF-8 characters in comments:
 *
 *	§ ¢ £ ® © ± ¹ ² ³ ·
 *	à á â ã ä å
 *	æ ç
 *	è é ê ë
 *	ì í î ï
 *	ð ñ
 *	ò ó ô õ ö
 *	ù ú û ü
 */

uniform vec4 foo;

void main() {
	gl_FragColor = foo;
}
