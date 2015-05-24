// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f;
    while(f) {  // condition should be boolean
    }
}
