/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * check_link: true
 * [end config]
 *
 * From the GLSL 1.30 spec section 7.1 (Vertex Shader Special
 * Variables):
 *
 *   The gl_ClipDistance array is predeclared as unsized and must be
 *   sized by the shader either redeclaring it with a size or indexing
 *   it only with integral constant expressions. This needs to size
 *   the array to include all the clip planes that are enabled via the
 *   OpenGL API; if the size does not include all enabled planes,
 *   results are undefined. The size can be at most
 *   gl_MaxClipDistances. The number of varying components (see
 *   gl_MaxVaryingComponents) consumed by gl_ClipDistance will match
 *   the size of the array, no matter how many planes are enabled. The
 *   shader must also set all values in gl_ClipDistance that have been
 *   enabled via the OpenGL API, or results are undefined. Values
 *   written into gl_ClipDistance for planes that are not enabled have
 *   no effect.
 *
 * This test checks that the an error occurs when the size of
 * gl_ClipDistance is implicit, and we try to access a non-existent
 * element (gl_ClipDistance[gl_MaxClipDistances]) using an integral
 * constant expression.
 */
#version 130

void main()
{
  gl_Position = gl_Vertex;
  gl_ClipDistance[gl_MaxClipDistances] = 1.0;
}
