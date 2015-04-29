/* [config]
 * expect_result: fail
 * glsl_version: 1.10
 * require_extensions: GL_ARB_draw_instanced
 * [end config]
 *
 * From the GL_ARB_draw_instanced spec:
 *
 *     "Change Section 7.1 "Vertex Shader Special Variables"
 *
 *     Add the following definition to the list of built-in variable
 *     definitions:
 *
 *          int gl_InstanceIDARB // read-only"
 *
 * There is no mention of gl_InstanceIDARB in fragment shaders or
 * geometry shaders.
 */
#extension GL_ARB_draw_instanced: require

int function()
{
  return gl_InstanceIDARB;
}
