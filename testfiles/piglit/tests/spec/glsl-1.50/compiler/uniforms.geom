// [config]
// expect_result: pass
// glsl_version: 1.50
// [end config]
//
// Verify that all uniforms defined in GLSL 1.50 core are accessible from
// geometry shaders.

void main()
{
  gl_Position = vec4(gl_DepthRange.near, gl_DepthRange.far, gl_DepthRange.diff,
                     0.0);
  EmitVertex();
}
