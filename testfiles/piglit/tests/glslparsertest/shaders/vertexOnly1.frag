// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    gl_ClipVertex = vec4(1);  // can be used in vertex shader only
}
