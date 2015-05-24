// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - structure name conflicts with function name */

vec4 foo(vec4 a, vec4 b)
{
  return vec4(dot(a, b));
}

struct foo {
  float f;
  int i;
  bool b;
};
