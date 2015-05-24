/* [config]
 * expect_result: fail
 * glsl_version: 1.50
 * check_link: true
 * [end config]
 *
 * This test checks that an error occurs when the size of the
 * gl_ClipDistance input is implicit, and we try to access a
 * non-existent element
 * (gl_in[0].gl_ClipDistance[gl_MaxClipDistances]) using an integral
 * constant expression.
 */
#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

void main()
{
  gl_Position = vec4(gl_in[0].gl_ClipDistance[gl_MaxClipDistances]);
  EmitVertex();
}
