// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    vec4 v;
    vec4 v1 = (vec4) v; // incorrect typecasting, vec4(v) is correct  
}
