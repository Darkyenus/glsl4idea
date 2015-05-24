// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

#extension foo : //  behavior not specified

void main()
{
}
