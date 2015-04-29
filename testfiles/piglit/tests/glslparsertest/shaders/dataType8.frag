// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

varying float f;
void main()
{
    f = 1.0;  // varyings cannot be written to in a fragment shader, they can be written to in a vertex shader
}
