// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void main()
{
	vec2 v = vec2(1.0, 2.0);
	v *= 2.0; // Legal in GLSL.
}
