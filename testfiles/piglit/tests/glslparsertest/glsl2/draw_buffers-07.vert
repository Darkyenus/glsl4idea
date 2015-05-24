// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - gl_MaxDrawBuffers is available, but gl_FragData is only in fragment
 * shaders
 */
#version 110

void main()
{
  gl_FragData[0] = gl_Vertex;
  gl_Position = gl_Vertex;
}
