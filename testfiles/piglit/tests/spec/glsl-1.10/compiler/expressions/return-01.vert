// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]

/* The ARB_shading_language_420pack, GLSL ES 3.0, and GLSL 4.20
 * specs add a clarification:
 *
 *     "A void function can only use return without a return argument, even if
 *      the return argument has void type. Return statements only accept values:
 *
 *          void func1() { }
 *          void func2() { return func1(); } // illegal return statement"
 *
 * Verify that returning void from a void function is illegal. Since it is a
 * clarification rather than a spec change, this behavior is expected even
 * without ARB_shading_language_420pack or before GLSL ES 3.0 or GLSL 4.20.
 */

#ifdef GL_ES
precision mediump float;
#endif

void A(int i) { }

void B(int i) {
	return A(i);
}

attribute vec4 vertex;

void main() {
	B(1);
	gl_Position = vertex;
}
