/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 21 (page 27 of the PDF) of the GLSL 1.20 spec:
 *
 *     "An implicitly sized array can be re-declared in the same scope
 *     as an array of the same base type."
 */
#version 120

attribute vec4 v;

void main()
{
  float a[];

  a[3] = 1.2;   // Implicitly size "a" to have 4 elements.

  {
    float a[4]; // this declaration shadows the previous
  }

  a.length();   // illegal - "a' is not explicitly sized

  gl_Position = v;
}
