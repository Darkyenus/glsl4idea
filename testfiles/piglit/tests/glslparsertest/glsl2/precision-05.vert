// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]

#version 130

/* Keyword 'precision' is required for this to set the default precision.
 * However, the language allows empty declarations with or without a precision
 * qualifier.  Other shipping implementations allow this syntax, and there is
 * nothing in the spec that forbids it.
 */
lowp float;
mediump int;
