// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - implicit conversions are not allowed in GLSL 1.10 */
struct s { float f; };

void main()
{
    s t = s(1);
}
