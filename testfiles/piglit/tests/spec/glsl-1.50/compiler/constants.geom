// [config]
// expect_result: pass
// glsl_version: 1.50
// [end config]
//
// Verify that all constants defined in GLSL 1.50 core are accessible from
// geometry shaders.

#version 150

void main()
{
  int i = 0;
  i += gl_MaxVertexAttribs;
  i += gl_MaxVertexUniformComponents;
  i += gl_MaxVaryingFloats;
  i += gl_MaxVaryingComponents;
  i += gl_MaxVertexOutputComponents;
  i += gl_MaxGeometryInputComponents;
  i += gl_MaxGeometryOutputComponents;
  i += gl_MaxFragmentInputComponents;
  i += gl_MaxVertexTextureImageUnits;
  i += gl_MaxCombinedTextureImageUnits;
  i += gl_MaxTextureImageUnits;
  i += gl_MaxFragmentUniformComponents;
  i += gl_MaxDrawBuffers;
  i += gl_MaxClipDistances;
  i += gl_MaxGeometryTextureImageUnits;
  i += gl_MaxGeometryOutputVertices;
  i += gl_MaxGeometryTotalOutputComponents;
  i += gl_MaxGeometryUniformComponents;
  i += gl_MaxGeometryVaryingComponents;
  gl_Position = vec4(i);
  EmitVertex();
}
