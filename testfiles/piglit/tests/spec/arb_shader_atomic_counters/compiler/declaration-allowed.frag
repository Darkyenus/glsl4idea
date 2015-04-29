/* [config]
 * expect_result: pass
 * glsl_version: 1.40
 * require_extensions: GL_ARB_shader_atomic_counters
 * [end config]
 *
 * "[Atomic counters] can only be declared as function parameters or
 *  uniform-qualified global variables."
 */
#version 140
#extension GL_ARB_shader_atomic_counters: require

layout(binding=0) uniform atomic_uint x;

void f(atomic_uint y)
{
}

void main()
{
}
