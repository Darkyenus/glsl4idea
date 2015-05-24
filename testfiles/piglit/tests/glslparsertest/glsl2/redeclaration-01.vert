// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS
 *
 * This test reproduces the failure reported in bugzilla #29607.
 */
const float exp = 1.0;

void main()
{
  const float exp = 2.0;
  gl_Position = vec4(0.0);
}
