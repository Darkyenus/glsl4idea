// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS
 *
 * From page 11 (page 17 of the PDF) of the GLSL 1.10 spec:
 *
 *     "If an implementation does not recognize the tokens following #pragma,
 *     then it will ignore that pragma."
 */
#pragma unlikely_to_be_recognized

void main()
{
  gl_Position = gl_Vertex;
}
