// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    mat4 m;
    mat4 m1 = mat4(m);
}
