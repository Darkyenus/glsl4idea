// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//
// "centroid" is a reserved word in GLSL 1.20
#version 120

uniform vec4 centroid;

void main()
{
  gl_Position = centroid;
}
