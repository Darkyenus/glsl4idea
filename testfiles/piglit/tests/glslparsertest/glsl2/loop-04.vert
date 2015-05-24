// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */

void main()
{
  float i = gl_Vertex.x;

  while (bool i = true)
    /* empty */ ;

  gl_Position = gl_Vertex;
}
