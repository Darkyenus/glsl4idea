/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * For ARB_arrays_of_arrays spec:
 *
 * "For unsized arrays, only the outermost dimension can be lacking a
 *  size. A type that includes an unknown array size cannot be formed into
 *  an array until it gets an explicit size."
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

uniform vec4[1][] an_array;

void main()
{
  gl_Position = an_array[0][0];
}
