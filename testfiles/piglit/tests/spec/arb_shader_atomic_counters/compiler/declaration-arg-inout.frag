/* [config]
 * expect_result: fail
 * glsl_version: 1.40
 * require_extensions: GL_ARB_shader_atomic_counters
 * [end config]
 *
 * "Counters cannot be treated as l-values; hence cannot be used as
 *  out or inout function parameters, nor can they be assigned into."
 */
#version 140
#extension GL_ARB_shader_atomic_counters: require

layout(binding=0) uniform atomic_uint x;

void f(inout atomic_uint y)
{
}

void main()
{
}
