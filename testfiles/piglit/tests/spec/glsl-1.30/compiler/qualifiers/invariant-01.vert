// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// "invariant" is a reserved word in GLSL 1.30
#version 130

uniform vec4 invariant;

void main()
{
  gl_Position = invariant;
}
