// [config]
// expect_result: fail
// glsl_version: 1.40
// [end config]
#version 140

int func()
{
	/* This value (number of fixed function texture environment
	 * units) was erroneously left in the core profile on release
	 * of GLSL 1.40, similar to the typo of increasing the minimum
	 * from 2 to 16 in GLSL 1.30.  In GLSL 1.50, a later
	 * clarification moved it to to the compatibility profile, but
	 * that was never backported to 1.40.
	 */
	return gl_MaxTextureUnits;
}
