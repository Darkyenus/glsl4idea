// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    int i = 3;
    float f[i]; // arrays should be declared with a constant size
}
