// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - x is redeclared in the function body at the same scope as the
 *        parameter
 */
void a(float x, float y)
{
	float x;

	x = y;
}
