// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    const int i = 5;
    i++;  // const cannot be modified
}
