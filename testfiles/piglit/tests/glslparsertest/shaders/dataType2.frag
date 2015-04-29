// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

uniform sampler1D samp1;
uniform sampler1D samp2 = samp1; // uniforms are read only

void main()
{
}
