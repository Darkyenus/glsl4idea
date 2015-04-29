// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

#version 120
void main()
{
   const float length = 5;
   const bool no = length == 2;
}
