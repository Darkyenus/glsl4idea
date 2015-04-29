/* [config]
 * expect_result: fail
 * glsl_version: 1.40
 * require_extensions: GL_ARB_shader_atomic_counters
 * [end config]
 *
 * "[Atomic counters] can only be declared as function parameters or
 *  uniform-qualified global variables."
 */
#version 140
#extension GL_ARB_shader_atomic_counters: require

const atomic_uint x;

void main()
{
}
