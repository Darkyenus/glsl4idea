// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// check_link: false
// [end config]
//
// ARB_gpu_shader5 spec says:
//   "A default stream number may be declared at global
//    scope by qualifying interface qualifier out as in this example:
//
//          layout(stream = 1) out;
//
//    The stream number specified in such a declaration replaces any previous
//    default and applies to all subsequent block and variable declarations
//    until a new default is established.  The initial default stream number is
//    zero."
//
// Tests for multiple declarations of layout qualifier 'stream'.
//

#version 150
#extension GL_ARB_gpu_shader5 : enable

layout(points) in;
layout(points) out;

out vec4 var1;
layout(stream=1) out;
out vec4 var2;
layout(stream=2) out vec3 var3;

layout(stream=3) out Block1 {
	float var4;
	layout(stream=3) vec4 var5;
};

void main()
{
}
