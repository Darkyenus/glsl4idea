// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// "out" is only allowed in parameter list or at global scope in GLSL 1.30
#version 130

in vec4 vertex;

void main()
{
  out vec4 foo;

  gl_Position = vertex;
}
