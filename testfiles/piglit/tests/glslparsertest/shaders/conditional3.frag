// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f1,f2;
    bool b;
    int i = b ? f1 : f2; // second and third expression type does not match the lvalue type
}
