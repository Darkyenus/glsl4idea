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
//
// Note also that the text above makes no provision about arrays of
// integers; this is presumably an oversight, since there is no
// reasonable way to interpolate a fragment shader input that contains
// an integer.

#version 130

in int[2] x;

void main()
{
	gl_FragColor = vec4(float(x[0]), float(x[1]), 0.0, 0.0);
}
