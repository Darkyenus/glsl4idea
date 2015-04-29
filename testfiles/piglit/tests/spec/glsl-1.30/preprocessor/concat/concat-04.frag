// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that the preprocessor concatenation operator performs macro
// expansion after token pasting, not before.
//
// From page 11 (17 of pdf) of the GLSL 1.30 spec:
//     "The result must be a valid single token, which will then be subject to
//     macro expansion. That is, macro expansion happens after token pasting
//     and does not happen before token pasting."

#version 130

#define I i
#define N n
#define T t
#define INT invalid_type
#define INVALID_TYPE I ## N ## T

int f()
{
	// Correct behavior:
	// Macro expansion happens after token pasting, and INVALID_TYPE
	// expands to invalid_type. Compilation fails.
	//
	// Incorrect  behavior:
	// Macro expansion happens before, and INVALID_TYPE expands to int.
	// Compilation succeeds.
	INVALID_TYPE x;
	return 0;
}
