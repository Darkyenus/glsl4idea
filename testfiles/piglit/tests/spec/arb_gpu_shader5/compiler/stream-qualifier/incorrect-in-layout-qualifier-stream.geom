// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// check_link: false
// [end config]
//
// Tests for invalid input layout qualifiers.
//

#version 150
#extension GL_ARB_gpu_shader5 : enable

layout(points, stream=1) in;
layout(triangle_strip, max_vertices=3) out;

void main()
{
}
