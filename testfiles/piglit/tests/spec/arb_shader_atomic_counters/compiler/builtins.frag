/* [config]
 * expect_result: pass
 * glsl_version: 1.40
 * require_extensions: GL_ARB_shader_atomic_counters
 * [end config]
 *
 * Check that the builtin constants defined by the extension
 * are present.
 */
#version 140
#extension GL_ARB_shader_atomic_counters: require

out ivec4 fcolor;

void main()
{
        fcolor.x = gl_MaxVertexAtomicCounters +
                gl_MaxTessControlAtomicCounters +
                gl_MaxTessEvaluationAtomicCounters +
                gl_MaxGeometryAtomicCounters +
                gl_MaxFragmentAtomicCounters +
                gl_MaxCombinedAtomicCounters +
                gl_MaxAtomicCounterBindings;
}
