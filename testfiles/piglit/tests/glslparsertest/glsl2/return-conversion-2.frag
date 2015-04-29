// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - no implicit conversions for return values */
vec2 foo()
{
   float x = 2.0;
   return x;
}
void main()
{
   gl_FragColor = vec4(0.0, foo(), 0.0);
}
