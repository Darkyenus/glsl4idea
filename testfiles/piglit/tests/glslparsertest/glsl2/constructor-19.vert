// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

/* PASS */
#version 120

void main()
{
  struct s { int i; };

  s temp[2] = s[](s(1), s(2));
}
