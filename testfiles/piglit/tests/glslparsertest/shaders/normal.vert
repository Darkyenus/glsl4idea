// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    gl_Normal = vec3(1.0,2.0,3.0);  // cannot be modified an attribute
}
