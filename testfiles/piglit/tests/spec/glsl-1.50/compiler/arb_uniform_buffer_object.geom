/* [config]
 * expect_result: pass
 * glsl_version: 1.50
 * require_extensions: GL_ARB_uniform_buffer_object
 * [end config]
 *
 * Verify that GL_ARB_uniform_buffer_object can be used in geometry shaders
 */
#version 150
#extension GL_ARB_uniform_buffer_object: require

uniform a {
  vec4 b;
};

void main()
{
  gl_Position = b;
  EmitVertex();
}
