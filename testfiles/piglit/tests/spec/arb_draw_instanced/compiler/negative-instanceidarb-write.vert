/* [config]
 * expect_result: fail
 * glsl_version: 1.10
 * require_extensions: GL_ARB_draw_instanced
 * [end config]
 *
 * From the GL_ARB_draw_instanced spec:
 *
 *     "int gl_InstanceIDARB // read-only
 *
 *      Add the following paragraph at the end of the section:
 *
 *      The variable gl_InstanceIDARB is available as a read-only
 *      variable from within vertex shaders and holds holds the
 *      integer index of the current primitive in an instanced draw
 *      call (DrawArraysInstancedARB, DrawElementsInstancedARB)."
 *
 * So we check that trying to write to it fails to compile.
 */
#extension GL_ARB_draw_instanced: require

void function()
{
  gl_InstanceIDARB = 0;
}
