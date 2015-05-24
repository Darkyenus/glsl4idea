// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// check_link: false
// [end config]
//
// Tests for invalid output layout qualifiers.
//

#version 150
#extension GL_ARB_gpu_shader5 : enable

layout(points) in;
layout(triangle_strip, max_vertices=3, invocations=1) out;

void main()
{
}
