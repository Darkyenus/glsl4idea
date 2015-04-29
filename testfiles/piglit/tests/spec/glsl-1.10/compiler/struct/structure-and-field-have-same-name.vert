/* [config]
 * expect_result: pass
 * glsl_version: 1.10
 * [end config]
 *
 * This is related to the WebGL shader-with-non-reserved-words tests.  Several
 * of these tests use name that are reserved words in other GLSL versions as
 * variable names, structure names, structure field names, and function
 * names.  Some of the cases use the same name for the structure name and the
 * field name.
 */
struct S { vec4 S; };

void main()
{
  S s = S(gl_Vertex);
  gl_Position = s.S;
}
