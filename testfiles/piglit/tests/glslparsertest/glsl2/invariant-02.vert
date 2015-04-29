// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

/* PASS -
 *
 * From page 27 (page 33 of the PDF) of the GLSL 1.20 spec:
 *
 *     "The invariant keyword can be followed by a comma separated list of
 *     previously declared identifiers."
 */
#version 120

invariant gl_Position, gl_FrontColor, gl_BackColor;

void main() { }
