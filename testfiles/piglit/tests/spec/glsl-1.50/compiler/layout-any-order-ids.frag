// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Section 4.3.8(Layout Qualifiers) of the GLSL 1.50 spec says:
// "The tokens in any layout-qualifier-id-list are identifiers, not keywords.
//  Generally, they can be listed in any order. Order-dependent meanings exist
//  only if explicitly called out below. Similarly, these identifiers are not
//  case sensitive, unless explicitly noted otherwise."

#version 150

layout(pixel_center_integer, origin_upper_left) in vec4 gl_FragCoord;
layout(origin_upper_left, pixel_center_integer) in vec4 gl_FragCoord;

void main()
{
	gl_FragColor = vec4(0., 1., 0., 1.);
}
