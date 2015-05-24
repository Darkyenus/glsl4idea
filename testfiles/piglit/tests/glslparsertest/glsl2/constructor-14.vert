// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
uniform mat2 m;

void main()
{
  ivec4 v4 = ivec4(m);
}
