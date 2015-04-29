// [config]
// expect_result: pass
// glsl_version: 1.10
// require_extensions: GL_ARB_explicit_attrib_location
// [end config]
//
// Even though the specified location is clearly too large, the spec says that
// a *link* error is generated.  The resolution to issue #1 in the
// GL_ARB_explicit_attrib_location spec clearly covers this:
//
//     "RESOLVED.  Generate a link error.  The existing spec language already
//      covers this case..."

#version 110
#extension GL_ARB_explicit_attrib_location: require
layout(location = 0x7fffffff) out vec4 color;

void main()
{
	color = vec4(1.0);
}
