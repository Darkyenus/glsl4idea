// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// "out" is only allowed in parameter list in GLSL 1.10

void main()
{
  out vec4 foo;

  gl_Position = gl_Vertex;
}
