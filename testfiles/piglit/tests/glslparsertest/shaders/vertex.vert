// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    gl_Vertex = vec4(1.0,2.0,3.0, 4.0); // cannot modify an attribute
}
