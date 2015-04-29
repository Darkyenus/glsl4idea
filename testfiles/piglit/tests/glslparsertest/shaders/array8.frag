// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f[5];
    float f[];  // redeclaration of array already declared with a size
}
