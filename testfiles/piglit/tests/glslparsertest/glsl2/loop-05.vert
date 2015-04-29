// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - loop body does not start a new scope */

void main()
{
  while (bool i = true)
    float i = gl_Vertex.x;

  gl_Position = gl_Vertex;
}
