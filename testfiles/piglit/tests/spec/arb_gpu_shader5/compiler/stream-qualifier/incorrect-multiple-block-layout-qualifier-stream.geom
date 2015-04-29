// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// check_link: false
// [end config]
//
// ARB_gpu_shader5 spec says:
//   "A block member may be declared with a stream
//    qualifier, but the specified stream must match the stream
//    associated with the containing block."
//
// Tests for multiple declarations of layout qualifier 'stream' for
// block's fields.
//

#version 150
#extension GL_ARB_gpu_shader5 : enable

layout(points) in;
layout(triangle_strip, max_vertices=3) out;

out Block1 { // By default, it uses stream = 0
	layout(stream=1) vec4 var1; // Wrong: different than block's stream value
	layout(stream=5) vec4 var2; // Wrong: different than block's stream value
	layout(stream=0) vec4 var3; // Valid
	vec4 var4; // Valid
};

void main()
{
}
