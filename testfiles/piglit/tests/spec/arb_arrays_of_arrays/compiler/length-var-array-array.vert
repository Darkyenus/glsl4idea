/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

uniform vec4 an_array[3][2];

int foo[an_array.length() == 3 ? 1 : -1];
int foo2[an_array[1].length() == 2 ? 1 : -1];
