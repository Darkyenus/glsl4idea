// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: false
// [end config]
//
// Test that noise functions are available to geometry shaders.

/*
 * GLSLLangSpec 1.50, section 8.9 (Noise Functions):
 * "Noise functions are available to fragment, geometry, and vertex shaders."
 */

#version 150

layout(points) in;
layout(points) out;

out float n;

void main()
{
	n = noise1(25);
}
