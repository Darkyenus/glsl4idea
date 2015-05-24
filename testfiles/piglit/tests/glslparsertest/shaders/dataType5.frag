// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f = 1; // int cannot be converted to float, use constructor to do the conversion explicitly
}
