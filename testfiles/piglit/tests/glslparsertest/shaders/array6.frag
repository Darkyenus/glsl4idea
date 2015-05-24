// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    const float index = 3.0;
    float f[index];  // arrays should be declared with an integer expression not float
}
