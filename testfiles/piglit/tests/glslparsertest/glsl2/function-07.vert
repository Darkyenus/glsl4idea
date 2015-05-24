// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - function does not return a value */

vec4 foo(in float x)
{
   vec4 v;
   v.x = x;
   v.y = x;
   v.z = x;
   v.w = x;
}

void main()
{
  gl_Position = foo(2.0);
}
