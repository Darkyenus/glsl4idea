// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

#version 120
uniform mat3 m3;
varying mat2 m2;
void main()
{
   m2 = mat2(m3);
}
