// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f[];
    int f[4];  // array redeclared with a different type
}
