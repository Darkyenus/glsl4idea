// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    ivec4 v4;
    v4 = v4 + 2.0;
}
