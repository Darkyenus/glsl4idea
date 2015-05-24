// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - invalid parameter type for function 'foo' */

void bar(foo x);

void main()
{
  gl_Position = vec4(0.0);
}
