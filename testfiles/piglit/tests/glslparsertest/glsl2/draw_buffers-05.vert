// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS - GL_ARB_draw_buffers does exist in the vertex shader, but it only
 * makes the built in variable gl_MaxDrawBuffers be available.
 */
#version 110

/* This causes an error on some implementations, but is acceptable on others.
 * The GL_ARB_draw_buffers spec only specifies behavior when #extension is
 * used with GL_ARB_draw_buffers in a fragment shader.  It doesn't say what
 * happens (i.e., error or otherwise) in a vertex shader.  The GLSL 1.10 spec
 * says that gl_MaxDrawBuffers is available in a vertex shader.  From that I
 * infer that using GL_ARB_draw_buffers with #extension should be valid in a
 * fragment shader.
 */
#extension GL_ARB_draw_buffers: require

void main()
{
  gl_Position = vec4(gl_MaxDrawBuffers);
}
