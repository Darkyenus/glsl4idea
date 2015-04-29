// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */

void main()
{
  while (bool i = true)
    /* empty */ ;

  gl_Position = gl_Vertex;
}
