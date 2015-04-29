// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - void parameter cannot have a name
 */
float a(void x)
{
  return x;
}
