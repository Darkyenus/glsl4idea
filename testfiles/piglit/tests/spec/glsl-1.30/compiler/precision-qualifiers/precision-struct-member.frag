// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Section 4.1.8 (Structures) of the GLSL 1.30 spec says:
//
//     "Member declarators may contain precision qualifiers, but may not
//     contain any other qualifiers."

#version 130

struct s {
	lowp float a;
};

void main() { gl_FragColor = vec4(0); }
