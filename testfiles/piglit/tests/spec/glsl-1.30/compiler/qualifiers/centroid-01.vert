// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// "centroid" is a reserved word in GLSL 1.30
#version 130

uniform vec4 centroid;

void main()
{
  gl_Position = centroid;
}
