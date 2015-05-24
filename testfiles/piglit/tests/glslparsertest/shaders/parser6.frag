// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f1,f2,f3;
    f3 = f1 > f2;  // f1 > f2 result in a bool that cannot be assigned to a float
}
