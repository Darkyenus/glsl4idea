// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

varying float f;
void main()
{
    float flt = 1.0;
    flt++;
    f++;  // varyings in a fragment shader are read only
}
