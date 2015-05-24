// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// check_link: false
// [end config]
//
// ARB_gpu_shader5 spec says:
//   "If an invocation count is declared, all such declarations must
//    specify the same count."
//
// Tests for multiple declarations of layout qualifier 'invocations'.
//

#version 150
#extension GL_ARB_gpu_shader5 : enable

layout(points, invocations=4) in;
layout(invocations=3) in;
layout(triangle_strip, max_vertices=3) out;

void main()
{
}
