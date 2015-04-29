// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// The C preprocessor is supposed to generate an error for division by zero,
// and the GLSL spec says that the GLSL preprocessor behaves like the C
// preprocessor.

#if 1 / 0
/* empty */
#endif

void main()
{
	gl_Position = vec4(0.0);
}
