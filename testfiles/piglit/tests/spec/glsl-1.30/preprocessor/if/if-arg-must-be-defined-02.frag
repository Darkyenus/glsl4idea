// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Check that the compiler treats an undefined macro as 0 when used as
// an argument to the #elif directive.
//
// Older GLSL specifications (such as GLSL 1.30) had the following language:
//
//     "It is an error to use #if or #elif on expressions containing
//     undefined macro names, other than as arguments to the
//     defined operator."
//
//     [Page 11 (17 of pdf) of the GLSL 1.30 spec]
//
// But GLSL 4.30 drops this, so that un undefined macro should be
// treated as 0 just as is standard for C preprocessors. Many
// implementations have been doing this already as it's what is
// standard for C preprocessors, so is expected by users.

#version 130

#if 0
#	define JUNK 1
#elif UNDEFINED_MACRO
#	define JUNK 1
#endif

#if defined JUNK
#error Undefined macro should be treated as 0.
#endif

int f()
{
	return 0;
}
