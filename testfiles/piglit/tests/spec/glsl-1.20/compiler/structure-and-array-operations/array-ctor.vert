/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * [end config]
 *
 * From page 20 (page 26 of the PDF) of the GLSL 1.20 spec:
 *
 *     "Arrays can have initializers formed from array constructors:"
 */
#version 120

vec4 a[2] = vec4[2](vec4(0.0), vec4(2.0));
vec4 b[2] = vec4[ ](vec4(0.5), vec4(2.0));
vec4 c[ ] = vec4[ ](vec4(1.0), vec4(2.0));

void main() { gl_Position = a[0] + b[0] + c[0]; }
