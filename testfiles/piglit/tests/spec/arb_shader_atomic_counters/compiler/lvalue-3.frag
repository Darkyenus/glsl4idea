/* [config]
 * expect_result: fail
 * glsl_version: 1.40
 * require_extensions: GL_ARB_shader_atomic_counters
 * [end config]
 *
 * "Counters cannot be treated as l-values."
 */
#version 140
#extension GL_ARB_shader_atomic_counters: require

layout(binding=0) uniform atomic_uint x;
layout(binding=0) uniform atomic_uint y;

void main()
{
	x = y;
}
