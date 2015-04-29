// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - cannot construct samplers */
void main()
{
  int i;

  i = sampler2D(0);
}
