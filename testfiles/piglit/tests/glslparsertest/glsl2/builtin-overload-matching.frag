// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]

/**
 * PASS
 *
 * This shader triggered a bug in Mesa's lazy built-in prototype importing.
 *
 * The first call, abs(f), would fail to find a local signature, look through
 * the built-ins, and import the signature
 *
 *    float abs(float);
 *
 * The second call, abs(i), would also look for a local signature first,
 * finding the float signature.  Unfortunately, it settled for this inexact
 * match, failing to search the built-ins to find the correct signature:
 *
 *    int abs(int);
 *
 * So abs(i) ended up being a float, leading to bizarre type errors.
 */
#version 130

uniform float f;
uniform int i;

void main()
{
    float af = abs(f);
    int ai = abs(i);

    gl_FragColor = vec4(af, af, ai, ai);
}
