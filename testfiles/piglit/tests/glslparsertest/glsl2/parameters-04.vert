// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - formal parameter lacks a name
 */
float a(float x, float )
{
  return x;
}
