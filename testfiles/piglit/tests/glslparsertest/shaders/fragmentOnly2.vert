// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    gl_FragCoord = vec4(1.0);  // can be used in fragment shader only
}
