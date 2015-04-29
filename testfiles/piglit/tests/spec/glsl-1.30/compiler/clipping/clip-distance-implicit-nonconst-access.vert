/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * [end config]
 *
 * From the GLSL 1.30 spec section 7.1 (Vertex Shader Special
 * Variables):
 *
 *   The gl_ClipDistance array is predeclared as unsized and must be
 *   sized by the shader either redeclaring it with a size or indexing
 *   it only with integral constant expressions.
 *
 * This test verifies that gl_ClipDistance is predeclared as unsized
 * by attempting to access it using a non-constant index, an operation
 * that is not allowed on unsized arrays.
 */
#version 130
uniform int index;

void main()
{
  gl_Position = vec4(0.0);
  gl_ClipDistance[index] = 0.0;
}
