// [config]
// expect_result: fail
// glsl_version: 3.00 es
// check_link: true
// [end config]
//
// Declare a non-flat integral fragment input.
//
// From section 4.3.4 ("Input Variables") of the GLSL ES 3.00 spec:
//    "Fragment shader inputs that are, or contain, signed or unsigned
//    integers or integer vectors must be qualified with the
//    interpolation qualifier flat."

#version 300 es

in ivec4 x;
out highp vec4 color;

void main()
{
	color = vec4(x);
}
