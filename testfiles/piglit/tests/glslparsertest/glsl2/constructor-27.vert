// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - too many parameters given to structure constructor */
struct s { float f; };

void main()
{
    s t = s(1.0, 2.0);
}
