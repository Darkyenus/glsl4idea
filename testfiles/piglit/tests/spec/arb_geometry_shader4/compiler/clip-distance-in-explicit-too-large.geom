/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * check_link: true
 * require_extensions: GL_ARB_geometry_shader4
 * [end config]
 *
 * This test checks that an error occurs when explicitly setting the
 * size of gl_ClipDistanceIn to [3][gl_MaxClipDistances+1], which
 * should generate an error.
 */
#version 130
#extension GL_ARB_geometry_shader4: enable

in float gl_ClipDistanceIn[3][gl_MaxClipDistances+1];

void main()
{
  gl_Position = vec4(0.0);
  EmitVertex();
}
