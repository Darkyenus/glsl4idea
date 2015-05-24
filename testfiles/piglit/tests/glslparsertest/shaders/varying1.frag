// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

varying float foo;

void main()
{
    foo = 5.0; // varying cannot be written by a fragment shader
}
