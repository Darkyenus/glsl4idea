/* [config]
 * expect_result: fail
 * glsl_version: 1.10
 * [end config]
 */


// This test checks that the compiler generates an error if we try
// declare two variables and a function with the same name.
// NVIDIA's 325.15 driver crashes on this.

varying float color;

float foo;

// Redeclaring foo here should generate an error
int foo;

// This causes NVIDIA's driver to crash:
vec4 foo(float v)
{
   return vec4(v);
}

void main()
{
	gl_FragColor = color;
}

