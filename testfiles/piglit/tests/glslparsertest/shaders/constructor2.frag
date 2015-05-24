// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    vec3 v;
    vec4 v1 = vec4(v); // insufficient data specified for construction
}
