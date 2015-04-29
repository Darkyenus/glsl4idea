// [config]
// expect_result: fail
// glsl_version: 1.40
// [end config]
#version 140

vec4 func()
{
	return gl_FrontLightModelProduct.sceneColor;
}
