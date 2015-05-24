// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]
//
// The gl_PerVertex input interface block only exists in geometry
// shaders.  Check that it may not be redeclared in vertex shaders.

#version 150

in gl_PerVertex {
  vec4 gl_Position;
} gl_in[];

void main()
{
}
