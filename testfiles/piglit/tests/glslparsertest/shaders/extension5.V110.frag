// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

#extension all : ddisablee  // error, behavior is not supported

void main()
{
}
