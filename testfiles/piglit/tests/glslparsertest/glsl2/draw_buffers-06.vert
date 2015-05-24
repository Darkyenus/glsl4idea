// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS - GL_ARB_draw_buffers does exist in the vertex shader, but it only
 * makes the built in variable gl_MaxDrawBuffers be available.
 */
#version 110

void main()
{
  gl_Position = vec4(gl_MaxDrawBuffers);
}
