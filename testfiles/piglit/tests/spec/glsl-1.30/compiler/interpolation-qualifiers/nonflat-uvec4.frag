// [config]
// expect_result: fail
// glsl_version: 1.30
// check_link: true
// [end config]
//
// Declare a non-flat integral fragment input.
//
// From section 4.3.4 ("Inputs") of the GLSL 1.50 spec:
//    "Fragment shader inputs that are signed or unsigned integers or
//    integer vectors must be qualified with the interpolation qualifier
//    flat."
//
// Note that prior to GLSL 1.50, this requirement is applied to vertex
// outputs rather than fragment inputs.  That creates problems in the
// presence of geometry shaders, so we assume the implementation
// should adopt the GLSL 1.50 rule for all desktop GL shaders.

#version 130

in uvec4 x;

void main()
{
	gl_FragColor = vec4(x);
}
