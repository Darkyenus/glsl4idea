/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * check_link: true
 * require_extensions: GL_ARB_geometry_shader4
 * [end config]
 *
 * This test verifies that gl_ClipDistanceIn is predeclared as unsized
 * by attempting to take the length of gl_ClipDistanceIn[0], an
 * operation that is not allowed on unsized arrays.
 */
#version 130
#extension GL_ARB_geometry_shader4: enable

void main()
{
  gl_Position = vec4(gl_ClipDistanceIn[0].length());
  EmitVertex();
}
