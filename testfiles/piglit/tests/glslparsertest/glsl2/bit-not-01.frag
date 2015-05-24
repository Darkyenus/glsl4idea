// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - bitwise operations aren't supported in 1.10. */
void main()
{
    int x = ~0 - 1;
}
