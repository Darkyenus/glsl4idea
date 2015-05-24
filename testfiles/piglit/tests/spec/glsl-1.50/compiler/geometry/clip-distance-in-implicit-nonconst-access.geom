/* [config]
 * expect_result: fail
 * glsl_version: 1.50
 * check_link: true
 * [end config]
 *
 * This test verifies that the gl_ClipDistance input is predeclared as
 * unsized by attempting to access it using a non-constant index, an
 * operation that is not allowed on unsized arrays.
 */
#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

uniform int index;

void main()
{
  gl_Position = vec4(gl_in[0].gl_ClipDistance[index]);
  EmitVertex();
}
