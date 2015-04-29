// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - Names beginning with "gl_" are reserved.
 *
 * Section 3.7 of the GLSL 1.20 spec says, without qualification:
 *
 *     "Identifiers starting with "gl_" are reserved for use by OpenGL, and
 *     may not be declared in a shader as either a variable or a function."
 *
 * The GLSL compiler in Nvidia's 195.36.15 driver allows this shader to
 * compile.  This is presumably on the basis the names of built-ins, gl_Vertex
 * in this case, can be overridden.
 */

float gl_Vertex(float x)
{
  return pow(x, 2.1718281828);
}

void main()
{
  float exp = gl_Vertex(2.0);
  gl_Position = vec4(0.0);
}
