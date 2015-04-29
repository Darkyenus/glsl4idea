// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

#version 120
uniform mat2 m2;
varying mat4 m4;
void main()
{
   m4 = mat4(m2);
}
