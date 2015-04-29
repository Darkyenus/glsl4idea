// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    vec2 v;
    v.xy = 1.2; // swizzle needs two values, v.xy = vec2(1.2) is correct
}
