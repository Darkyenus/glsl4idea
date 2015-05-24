// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f[3];
    f[3] = 1.0; // index of array greater than the size of the array
}
