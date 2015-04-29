// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f[5];
    f[];  // array used without a size
}
