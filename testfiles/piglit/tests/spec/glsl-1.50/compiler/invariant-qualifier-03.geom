// [config]
// expect_result: fail
// glsl_version: 1.50
// [end config]
//
// Verify that the invariant redeclarations cannot be used on uniforms in the GS.

#version 150

uniform vec4 junk;
invariant junk;	/* not allowed */
