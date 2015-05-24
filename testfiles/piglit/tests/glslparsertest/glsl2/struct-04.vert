// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */

struct foo {
  float f;
  int i;
  bool b;
};

void main()
{
  foo foo;

  gl_Position = gl_Vertex;
}
