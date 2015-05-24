// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS - built-in exp is outside the global scope
 *
 * See also redeclaration-04.vert.
 */

float exp(float x, float y)
{
    return x + y;
}

void main()
{
    gl_Position = vec4(0.0);
}
