/* [config]
 * expect_result: fail
 * glsl_version: 1.40
 * require_extensions: GL_ARB_shader_atomic_counters
 * [end config]
 *
 * "Except for array indexing, structure field selection, and
 *  parenthesis, counters are not allowed to be operands in
 *  expressions."
 */
#version 140
#extension GL_ARB_shader_atomic_counters: require

layout(binding=0) uniform atomic_uint x;

out ivec4 fcolor;

void main()
{
	fcolor.x = x;
}
