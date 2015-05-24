// [config]
// expect_result: fail
// glsl_version: 1.50
// [end config]
//
// Verify that the invariant-qualifier cannot be used on uniforms in the GS.

#version 150

invariant uniform vec4 junk;	/* not allowed */
