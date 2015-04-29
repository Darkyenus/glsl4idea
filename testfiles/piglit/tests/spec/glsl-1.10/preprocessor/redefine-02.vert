// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]

#define A 1
#define A

/* Some compilers generate spurious errors if a shader does not contain
 * any code or declarations.
 */
int foo(void) { return 1; }
