#version 300 es

/* [config]
 * expect_result: fail
 * glsl_version: 3.00
 * [end config]
 *
 * Section 3.4 (Preprocessor) of the OpenGL ES Shading Language 3.00 spec
 * says:
 *
 *     "It is an error to undefine or to redefine a built-in (pre-defined)
 *     macro name."
 */

#undef __FILE__

void main() { gl_Position = vec4(0); }
