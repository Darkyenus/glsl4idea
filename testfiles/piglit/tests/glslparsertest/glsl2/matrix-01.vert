// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - non-square matrices are not available in GLSL 1.10 */

void main()
{
    mat2x3 m;
}
