// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - return types cannot have qualifiers */
const float one()
{
   return 1.0;
}
void main()
{
   gl_FragColor = vec4(0.0, one(), 0.0, 0.0);
}
