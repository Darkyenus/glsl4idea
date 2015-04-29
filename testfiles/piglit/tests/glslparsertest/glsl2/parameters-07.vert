// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - cannot have multiple void parameters
 */
float a(void, void);

void main()
{
  gl_Position = gl_Vertex;
}
