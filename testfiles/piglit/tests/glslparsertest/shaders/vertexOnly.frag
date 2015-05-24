// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    gl_PointSize = 4.0;  // can be used in vertex shader only
}
