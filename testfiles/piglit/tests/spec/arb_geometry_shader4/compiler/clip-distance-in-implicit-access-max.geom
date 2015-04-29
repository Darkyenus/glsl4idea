/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * check_link: true
 * require_extensions: GL_ARB_geometry_shader4
 * [end config]
 *
 * This test checks that an error occurs when the size of
 * gl_ClipDistanceIn is implicit, and we try to access a non-existent
 * element (gl_ClipDistanceIn[0][gl_MaxClipDistances]) using an
 * integral constant expression.
 */
#version 130
#extension GL_ARB_geometry_shader4: enable

void main()
{
  gl_Position = vec4(gl_ClipDistanceIn[0][gl_MaxClipDistances]);
  EmitVertex();
}
