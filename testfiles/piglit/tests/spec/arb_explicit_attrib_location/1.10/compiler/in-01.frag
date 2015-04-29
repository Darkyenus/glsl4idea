// [config]
// expect_result: pass
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// "in" is allowed in shader input declarations in GLSL 1.10 when
// GL_ARB_explicit_attrib_location is enabled

#version 110
#extension GL_ARB_explicit_attrib_location: require
in vec4 color;

void main()
{
	gl_FragColor = color;
}
