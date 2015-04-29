// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    int i = 1.0;  // automatic type conversion does not take place, float cannot be converted to int
}
