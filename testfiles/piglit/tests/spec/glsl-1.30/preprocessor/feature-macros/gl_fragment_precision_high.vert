// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// From section 4.5.4 of the GLSL 1.30 spec:
//     "The built-in macro GL_FRAGMENT_PRECISION_HIGH is defined to 1:
//         #define GL_FRAGMENT_PRECISION_HIGH 1
//     This macro is available in both the vertex and fragment languages."

#version 130

#if GL_FRAGMENT_PRECISION_HIGH != 1
#   error GL_FRAGMENT_PRECISION_HIGH != 1
#endif

float f() {
	return 0.0;
}
