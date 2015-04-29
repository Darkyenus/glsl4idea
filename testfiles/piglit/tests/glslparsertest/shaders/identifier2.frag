// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    int gl_int;  // identifier name cannot begin with "gl_"
}
