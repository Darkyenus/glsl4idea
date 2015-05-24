// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    5 += 5; // l-value missing
}
