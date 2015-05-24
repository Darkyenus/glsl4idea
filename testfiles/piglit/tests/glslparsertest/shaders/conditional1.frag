// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f1,f2;
    int i;
    float f3 = i ? f1 : f2;  // expression must be boolean and not int
}
