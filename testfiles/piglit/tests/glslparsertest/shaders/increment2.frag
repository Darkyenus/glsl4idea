// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    int i;
    (i+i)++;  // i+i is not an l-value
}
