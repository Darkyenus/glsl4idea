// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */

void main()
{
  vec4 a[];

  gl_Position = gl_Vertex;

  vec4 a[10];
}
