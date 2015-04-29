// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f1,f2;
    int i;
    bool b;
    float f3 = b ? i : f2; // second and third expression should of the type float
}
