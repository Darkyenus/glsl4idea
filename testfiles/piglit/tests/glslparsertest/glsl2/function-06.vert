// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */

vec4 foo(in float x, in float y, float z, float w)
{
  vec4 v;
  v.x = x;
  v.y = y;
  v.z = z;
  v.w = w;
  return v;
}

vec4 foo(in float x)
{
   vec4 v;
   v.x = x;
   v.y = x;
   v.z = x;
   v.w = x;
   return v;
}

void main()
{
  gl_Position = foo(1.0, 1.0, 1.0, 0.0);
  gl_Position = foo(2.0);
}
