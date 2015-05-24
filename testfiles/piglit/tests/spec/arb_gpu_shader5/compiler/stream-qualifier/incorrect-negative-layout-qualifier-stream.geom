// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// check_link: false
// [end config]
//
// Test to detect negative value of layout qualifier 'stream'
//
// From ARB_gpu_shader5 spec:
//
// "If an implementation supports <N> vertex streams, the
//     individual streams are numbered 0 through <N>-1"
//

#version 150
#extension GL_ARB_gpu_shader5 : enable

layout(points) in;
layout(points, stream=-2, max_vertices=3) out;

void main()
{
}
