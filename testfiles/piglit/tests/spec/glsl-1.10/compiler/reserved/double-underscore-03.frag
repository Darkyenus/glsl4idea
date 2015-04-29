// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]
//
// Check that variable names that contain a double underscore, and the double
// underscore is located in the middle of the variable name, are reserved,
//
// From page 14 (20 of pdf) of the GLSL 1.10 spec:
//     "In addition, all identifiers containing two consecutive underscores
//     (__) are reserved as possible future keywords."
//
// The intention is that names containing __ are reserved for internal use by
// the implementation, and names prefixed with GL_ are reserved for use by
// Khronos.  Names simply containing __ are dangerous to use, but should be
// allowed.

int f()
{
	int i__am__reserved;
	return 0;
}
