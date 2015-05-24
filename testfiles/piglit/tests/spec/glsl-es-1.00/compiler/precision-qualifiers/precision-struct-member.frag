// [config]
// expect_result: pass
// glsl_version: 1.00
// [end config]
//
// Section 4.1.8 (Structures) of the GLSL ES 1.00 spec says:
//
//     "Member declarators may contain precision qualifiers, but may not
//     contain any other qualifiers."

#version 100

struct s {
	lowp float a;
};

void main() { gl_FragColor = vec4(0); }
