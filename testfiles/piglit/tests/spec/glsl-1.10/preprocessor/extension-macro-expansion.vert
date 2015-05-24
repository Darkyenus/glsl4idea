/* [config]
 * expect_result: fail
 * glsl_version: 1.10
 * [end config]
 *
 * Page 13 (page 19 of the PDF) of the GLSL 1.10 spec says:
 *
 *     "Macro expansion is not done on lines containing #extension and #version
 *      directives."
 *
 * Therefore, FOO will not be replaced by a valid behavior token.
 */
#define FOO disable
#extension all : FOO

/* Some compilers generate spurious errors if a shader does not contain
 * any code or declarations.
 */
int foo(void) { return 1; }
