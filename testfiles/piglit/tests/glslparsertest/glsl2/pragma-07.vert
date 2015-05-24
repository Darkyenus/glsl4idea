// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS
 *
 * From page 11 (page 17 of the PDF) of the GLSL 1.10 spec:
 *
 *     "[#pragma debug] can only be used outside function definitions."
 *
 * and
 *
 *     "[#pragma optimize] can only be used outside function definitions."
 *
 * However, it says nothing about other pragmas.
 */
void main()
{
#pragma unlikely_to_be_recognized
  gl_Position = gl_Vertex;
}
