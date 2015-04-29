// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    gl_Fog.density = 1.0;  // cannot modify a uniform
}
