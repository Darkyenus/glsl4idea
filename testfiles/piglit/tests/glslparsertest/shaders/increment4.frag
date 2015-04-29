// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    int i;
    i++ = 5;  // i++ is not an l-value
}
