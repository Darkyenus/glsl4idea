// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    vec2 v;
    vec3 v3 = v.xyz; // v is a vec2 and does not have a z component
}
