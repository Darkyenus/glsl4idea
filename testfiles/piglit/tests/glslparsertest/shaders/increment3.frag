// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

uniform sampler1D sampler1d;

void main()
{
    sampler1d++;  // uniforms cannot be modified
}
