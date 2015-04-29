// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

attribute int i;  // attributes cannot be int or bool

void main()
{
    gl_Position = vec4(1);
}
