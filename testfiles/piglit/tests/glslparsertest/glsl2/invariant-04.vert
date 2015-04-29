// [config]
// expect_result: fail
// glsl_version: 1.20
//
// [end config]

/* FAIL -
 *
 * From page 27 (page 33 of the PDF) of the GLSL 1.20 spec:
 *
 *     "Only variables output from a vertex shader can be candidates for
 *     invariance....All uses of invariant must be at the global scope, and
 *     before any use of the variables being declared as invariant."
 */
#version 120

void main() 
{
  invariant vec4 x;

  x = gl_ModelViewMatrix * gl_Vertex;
  gl_Position = x + gl_Color;
}
