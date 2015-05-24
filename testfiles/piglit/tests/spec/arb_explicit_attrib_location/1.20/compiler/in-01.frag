// [config]
// expect_result: pass
// glsl_version: 1.20
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// "in" is allowed in shader input declarations in GLSL 1.20 when
// GL_ARB_explicit_attrib_location is enabled

#version 120
#extension GL_ARB_explicit_attrib_location: require
in vec4 color;

void main()
{
	gl_FragColor = color;
}
