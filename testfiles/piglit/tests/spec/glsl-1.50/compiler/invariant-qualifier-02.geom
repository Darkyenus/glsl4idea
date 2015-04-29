// [config]
// expect_result: fail
// glsl_version: 1.50
// [end config]
//
// Verify that the invariant-qualifier cannot be used on temps in the GS.

#version 150

invariant vec4 junk;	/* not allowed */
