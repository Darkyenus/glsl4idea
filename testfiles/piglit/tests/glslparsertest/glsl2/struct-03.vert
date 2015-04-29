// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */

vec4 foo(vec4 a, vec4 b)
{
  return vec4(dot(a, b));
}

void main()
{
  struct foo {
    float f;
    int i;
    bool b;
  };

  gl_Position = gl_Vertex;
}
