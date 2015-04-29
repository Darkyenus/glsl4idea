// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//
// "invariant" is a reserved word in GLSL 1.20
#version 120

uniform vec4 invariant;

void main()
{
  gl_Position = invariant;
}
