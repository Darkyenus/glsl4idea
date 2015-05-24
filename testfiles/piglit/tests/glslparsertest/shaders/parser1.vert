// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    int a  // semicolon missing at the end of the statement
    gl_Position = vec4(a);  
}
