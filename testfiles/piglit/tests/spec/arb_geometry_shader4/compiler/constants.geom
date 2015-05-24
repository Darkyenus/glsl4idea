// [config]
// expect_result: pass
// glsl_version: 1.10
// require_extensions: GL_ARB_geometry_shader4
// [end config]
//
// Verify that all constants defined in GLSL 1.10 are accessible from
// geometry shaders.

#extension GL_ARB_geometry_shader4: enable

void main()
{
  int i = 0;
  i += gl_MaxLights;
  i += gl_MaxClipPlanes;
  i += gl_MaxTextureUnits;
  i += gl_MaxTextureCoords;
  i += gl_MaxVertexAttribs;
  i += gl_MaxVertexUniformComponents;
  i += gl_MaxVaryingFloats;
  i += gl_MaxVertexTextureImageUnits;
  i += gl_MaxCombinedTextureImageUnits;
  i += gl_MaxTextureImageUnits;
  i += gl_MaxFragmentUniformComponents;
  i += gl_MaxDrawBuffers;
  gl_Position = vec4(i);
  EmitVertex();
}
