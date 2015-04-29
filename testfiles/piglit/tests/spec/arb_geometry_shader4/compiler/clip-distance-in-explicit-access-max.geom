/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * check_link: true
 * require_extensions: GL_ARB_geometry_shader4
 * [end config]
 *
 * This test checks that an error occurs when explicitly setting the
 * size of gl_ClipDistanceIn to [3][gl_MaxClipDistances] (which should
 * be ok) and trying to access a non-existent element
 * (gl_ClipDistance[0][gl_MaxClipDistances]) using an integral
 * constant expression, which should generate an error.
 */
#version 130
#extension GL_ARB_geometry_shader4: enable

in float gl_ClipDistanceIn[3][gl_MaxClipDistances];

void main()
{
  gl_Position = vec4(gl_ClipDistanceIn[0][gl_MaxClipDistances]);
  EmitVertex();
}
