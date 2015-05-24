// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    gl_FrontFacing = true;  // can be used in fragment shader only
}
