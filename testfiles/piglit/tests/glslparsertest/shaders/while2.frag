// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    while(float f = 5.0) {  // cannot declare variables in condition
    }
}
