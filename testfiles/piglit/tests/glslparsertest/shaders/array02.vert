// [config]
// expect_result: fail
// glsl_version: 1.20
// check_link: false
// [end config]
//
// Verify that out-of-bounds access to an array using a constant expression
// results in to compile error.

float array[5];
const int idx = -2;

void main()
{
   gl_Position = vec4(0.0, 1.0, 0.0, array[idx]);
}
