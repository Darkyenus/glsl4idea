/* [config]
 * expect_result: fail
 * glsl_version: 1.50
 * check_link: true
 * [end config]
 *
 * This test checks that an error occurs when explicitly setting the
 * size of the gl_ClipDistance input to gl_MaxClipDistances (which
 * should be ok) and trying to access a non-existent element
 * (gl_in[0].gl_ClipDistance[gl_MaxClipDistances]) using an integral
 * constant expression, which should generate an error.
 */
#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in gl_PerVertex {
  float gl_ClipDistance[gl_MaxClipDistances];
} gl_in[];

void main()
{
  gl_Position = vec4(gl_in[0].gl_ClipDistance[gl_MaxClipDistances]);
  EmitVertex();
}
