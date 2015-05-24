// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - type qualifiers mismatch between declaration and definition */

void foo(float x, float y, float z, out float l);

void foo(float x, float y, float z, inout float l)
{
  l = x + y + z;
}

void main()
{
  float x;
  foo(1.0, 1.0, 1.0, x);
  gl_Position = vec4(x);
}
