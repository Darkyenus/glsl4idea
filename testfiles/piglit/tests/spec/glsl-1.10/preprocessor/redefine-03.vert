// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]

#define A
#define A
#define B 1
#define B 1

/* Some compilers generate spurious errors if a shader does not contain
 * any code or declarations.
 */
int foo(void) { return 1; }
