// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    vec2 array[2];
    array.xy;  // arrays cannot directly be swizzled, however, an element of array can be swizzled
}
