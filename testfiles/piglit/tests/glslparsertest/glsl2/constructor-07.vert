// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */

uniform ivec2 a;
uniform ivec2 b;

void main()
{
  mat2 c;

  c = mat2(a, b);

  gl_Position = gl_Vertex;
}
