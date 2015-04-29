// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// check_link: true
// [end config]

/**
 * From issue 42:
 *
 * As a result, we choose a heavy-handed approach in which we only allow
 * calls to barrier() inside main().  Even within main, barrier() calls are
 * forbidden inside loops (even those that turn out to have constant loop
 * counts and don't execute "break" or "continue" statements), if
 * statements, or after a return statement.
 */

#version 150
#extension GL_ARB_tessellation_shader: require
layout(vertices = 3) out;
uniform bool cond;

void main() {
    gl_out[gl_InvocationID].gl_Position = vec4(0.0);
    if (cond)
        return;
    barrier();
    gl_TessLevelOuter = float[4](1.0, 1.0, 1.0, 1.0);
    gl_TessLevelInner = float[2](1.0, 1.0);
}
