// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Check that macro names end with a doule underscore are reserved.
//
// From page 11 (17 of pdf) of the GLSL 1.30 spec:
//     "All macro names containing two consecutive underscores ( __ ) are
//     reserved for future use as predefined macro names."
//
// The intention is that names containing __ are reserved for internal
// use by the implementation, and names prefixed with GL_ are reserved
// for use by Khronos.  Since every extension adds a name prefixed
// with GL_ (i.e., the name of the extension), that should be an
// error.  Names simply containing __ are dangerous to use, but should
// be allowed.  In similar cases, the C++ preprocessor specification
// says, "no diagnostic is required."

#version 130
#define I_AM_RESERVED__ 1

int f()
{
	return 0;
}
