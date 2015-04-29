// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f[-2]; // cannot declare arrays with negative size
}
