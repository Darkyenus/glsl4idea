// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main(int i)  // main function cannot take any parameters
{
    gl_Position = vec4(1);
}
