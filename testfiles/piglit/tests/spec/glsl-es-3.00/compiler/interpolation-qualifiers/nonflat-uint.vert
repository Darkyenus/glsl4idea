// [config]
// expect_result: fail
// glsl_version: 3.00 es
// check_link: true
// [end config]
//
// Declare a non-flat integral vertex output.
//
// From section 4.3.6 ("Output Variables") of the GLSL ES 3.00 spec:
//    "Vertex shader inputs that are, or contain, signed or unsigned
//    integers or integer vectors must be qualified with the
//    interpolation qualifier flat."

#version 300 es

out uint x;

void main()
{
	gl_Position = vec4(0.0);
	x = 1u;
}
