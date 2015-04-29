// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// Even though the specified locations overlap, the spec says that a *link*
// error is generated.  The changes to section 2.11.3 say:
//
//     "LinkProgram will fail if the attribute bindings assigned by
//     BindAttribLocation do not leave not enough space to assign a location
//     for an active matrix attribute or an active attribute array, both of
//     which require multiple contiguous generic attributes."
//
// However, vertex shader inputs cannot be arrays in GLSL 1.20.

#version 120
#extension GL_ARB_explicit_attrib_location: require
layout(location = 0) in vec4 vertex;
layout(location = 1) in vec4 colors[4];
layout(location = 2) in vec4 normal;

out vec4 varying_colors[4];
out vec4 n;

void main()
{
	gl_Position = vertex;
	varying_colors[0] = colors[0];
	varying_colors[1] = colors[1];
	varying_colors[2] = colors[2];
	varying_colors[3] = colors[3];
	n = normal;
}
