/* [config]
 * expect_result: pass
 * glsl_version: 1.10
 * require_extensions: GL_ARB_geometry_shader4 GL_ARB_uniform_buffer_object
 * [end config]
 *
 * Verify that GL_ARB_uniform_buffer_object can be used in geometry shaders
 */
#extension GL_ARB_geometry_shader4: require
#extension GL_ARB_uniform_buffer_object: require

uniform a {
  vec4 b;
};

void main()
{
  gl_Position = b;
  EmitVertex();
}
