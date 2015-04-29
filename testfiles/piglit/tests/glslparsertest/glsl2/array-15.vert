// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - array declared with a size is redeclared
 * The only case that where the spec seems to allow array redeclaration is when
 * the first declaration lacks a size.
 */

void main()
{
  vec4 a[10];

  gl_Position = gl_Vertex;

  vec4 a[10];
}
