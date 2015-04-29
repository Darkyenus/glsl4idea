// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL
 * 
 * GLSL 1.20 section 4.2 states:
 * "When a function name is redeclared in a nested scope, it hides all 
 *  functions declared with that name in the outer scope."
 *
 * Thus, declaring a new function exp(float, float) hides the builtin function.
 */
float exp(float x, float y)
{
    return x + y;
}

void main()
{
    float f = exp(2.0);
    gl_Position = vec4(0.0);
}
