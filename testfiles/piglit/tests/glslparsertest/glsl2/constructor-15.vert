// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
uniform mat2 m;

void main()
{
  int i = int(m);
}
