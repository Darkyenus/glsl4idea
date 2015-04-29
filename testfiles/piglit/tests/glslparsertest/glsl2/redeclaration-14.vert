// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

/* PASS - built-in exp is outside the global scope */
#version 120
uniform float exp;

void main()
{
    gl_Position = vec4(0.0);
}
