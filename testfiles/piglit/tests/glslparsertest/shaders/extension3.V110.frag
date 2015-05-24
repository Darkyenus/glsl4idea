// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

#extension foo : require  // error extension not supported 

void main()
{
}
