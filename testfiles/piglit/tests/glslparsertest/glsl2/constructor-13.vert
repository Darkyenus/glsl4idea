// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

#version 120
void main()
{
   const mat3x2 m = mat3x2(ivec3(1,2,3),ivec3(4,5,6));
}
