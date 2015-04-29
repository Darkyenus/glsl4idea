/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 33 (page 39 of the PDF) of the GLSL 1.20 spec:
 *
 *     "There must be exactly the same number of arguments as the size of the
 *     array being constructed."
 */
#version 120

vec4 a[] = vec4[2](vec4(0.0), vec4(1.0), vec4(2.0));

void main() { gl_Position = a[0]; }
