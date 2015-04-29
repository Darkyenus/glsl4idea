// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

attribute float f[2];  // attributes cannot be arrays

void main()
{
    gl_Position = vec4(1);
}
