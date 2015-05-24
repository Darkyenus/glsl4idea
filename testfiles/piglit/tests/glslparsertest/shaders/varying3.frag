// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

varying vec4 v = gl_TexCoord[0]; // varyings cannot be initialized

void main()
{
  
}
