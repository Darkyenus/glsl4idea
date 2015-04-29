// [config]
// expect_result: pass
// glsl_version: 1.10
// require_extensions: GL_ARB_geometry_shader4
// [end config]
//
// Verify that all uniforms defined in GLSL 1.10 are accessible from
// geometry shaders.

#extension GL_ARB_geometry_shader4: enable

void main()
{
  vec4 v = vec4(0.0);
  v += gl_ModelViewMatrix * gl_ProjectionMatrix *
       gl_ModelViewProjectionMatrix * gl_TextureMatrix[0] * vec4(1.0);
  v += vec4(gl_NormalMatrix * vec3(1.0), 0.0);
  v += gl_ModelViewMatrixInverse * gl_ProjectionMatrixInverse *
       gl_ModelViewProjectionMatrixInverse * gl_TextureMatrixInverse[0] *
       vec4(1.0);
  v += gl_ModelViewMatrixTranspose * gl_ProjectionMatrixTranspose *
       gl_ModelViewProjectionMatrixTranspose * gl_TextureMatrixTranspose[0] *
       vec4(1.0);
  v += gl_ModelViewMatrixInverseTranspose *
       gl_ProjectionMatrixInverseTranspose *
       gl_ModelViewProjectionMatrixInverseTranspose *
       gl_TextureMatrixInverseTranspose[0] * vec4(1.0);
  v += vec4(gl_NormalScale);
  v += vec4(gl_DepthRange.near, gl_DepthRange.far, gl_DepthRange.diff, 0.0);
  v += gl_ClipPlane[0];
  v += vec4(gl_Point.size, gl_Point.sizeMin, gl_Point.sizeMax,
            gl_Point.fadeThresholdSize);
  v += vec4(gl_Point.distanceConstantAttenuation,
            gl_Point.distanceLinearAttenuation,
            gl_Point.distanceQuadraticAttenuation, 0.0);
  v += gl_FrontMaterial.emission;
  v += gl_FrontMaterial.ambient;
  v += gl_FrontMaterial.diffuse;
  v += gl_FrontMaterial.specular;
  v += vec4(gl_FrontMaterial.shininess);
  v += gl_BackMaterial.emission;
  v += gl_BackMaterial.ambient;
  v += gl_BackMaterial.diffuse;
  v += gl_BackMaterial.specular;
  v += vec4(gl_BackMaterial.shininess);
  v += gl_LightSource[0].ambient;
  v += gl_LightSource[0].diffuse;
  v += gl_LightSource[0].specular;
  v += gl_LightSource[0].position;
  v += gl_LightSource[0].halfVector;
  v += vec4(gl_LightSource[0].spotDirection, gl_LightSource[0].spotExponent);
  v += vec4(gl_LightSource[0].spotCutoff, gl_LightSource[0].spotCosCutoff,
            gl_LightSource[0].constantAttenuation,
            gl_LightSource[0].linearAttenuation);
  v += vec4(gl_LightSource[0].quadraticAttenuation);
  v += gl_LightModel.ambient;
  v += gl_FrontLightModelProduct.sceneColor;
  v += gl_BackLightModelProduct.sceneColor;
  v += gl_FrontLightProduct[0].ambient;
  v += gl_FrontLightProduct[0].diffuse;
  v += gl_FrontLightProduct[0].specular;
  v += gl_BackLightProduct[0].ambient;
  v += gl_BackLightProduct[0].diffuse;
  v += gl_BackLightProduct[0].specular;
  v += gl_TextureEnvColor[0];
  v += gl_EyePlaneS[0];
  v += gl_EyePlaneT[0];
  v += gl_EyePlaneR[0];
  v += gl_EyePlaneQ[0];
  v += gl_ObjectPlaneS[0];
  v += gl_ObjectPlaneT[0];
  v += gl_ObjectPlaneR[0];
  v += gl_ObjectPlaneQ[0];
  v += gl_Fog.color;
  v += vec4(gl_Fog.density, gl_Fog.start, gl_Fog.end, gl_Fog.scale);
  gl_Position = v;
  EmitVertex();
}
