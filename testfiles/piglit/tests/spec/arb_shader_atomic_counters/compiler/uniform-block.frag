/* [config]
 * expect_result: fail
 * glsl_version: 1.40
 * require_extensions: GL_ARB_shader_atomic_counters
 * [end config]
 *
 * "Atomic counters may [...] NOT be grouped into uniform blocks."
 */
#version 140
#extension GL_ARB_shader_atomic_counters: require

uniform a {
  uniform atomic_uint x;
};

void main()
{
}
