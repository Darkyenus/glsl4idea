// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Section 4.3.8(Layout Qualifiers) of the GLSL 1.50 spec says:
// "Declarations of layouts can only be made at global scope"

#version 150

vec4 test(layout(pixel_center_integer) in float a)
{
	return vec4(a);
}

void main()
{
	gl_Position = test(.5);
}
