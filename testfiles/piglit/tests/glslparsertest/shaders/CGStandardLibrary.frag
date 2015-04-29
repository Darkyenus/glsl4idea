// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
	int i = round(1.3); // round is not a built-in function.
}
