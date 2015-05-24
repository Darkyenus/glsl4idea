/* [config]
 * expect_result: pass
 * glsl_version: 1.10
 * require_extensions: GL_ARB_geometry_shader4 GL_ARB_shader_texture_lod
 * [end config]
 *
 * Verify that GL_ARB_shader_texture_lod can be used in geometry shaders
 */
#extension GL_ARB_geometry_shader4: require
#extension GL_ARB_shader_texture_lod: require

uniform sampler1D s1;
uniform sampler2D s2;
uniform sampler3D s3;
uniform samplerCube sc;
uniform sampler1DShadow s1s;
uniform sampler2DShadow s2s;

void main()
{
  vec4 foo = texture1DGradARB(s1, 0.0, 0.0, 0.0);
  foo += texture1DProjGradARB(s1, vec2(0.0), 0.0, 0.0);
  foo += texture1DProjGradARB(s1, vec4(0.0), 0.0, 0.0);
  foo += texture2DGradARB(s2, vec2(0.0), vec2(0.0), vec2(0.0));
  foo += texture2DProjGradARB(s2, vec3(0.0), vec2(0.0), vec2(0.0));
  foo += texture2DProjGradARB(s2, vec4(0.0), vec2(0.0), vec2(0.0));
  foo += texture3DGradARB(s3, vec3(0.0), vec3(0.0), vec3(0.0));
  foo += texture3DProjGradARB(s3, vec4(0.0), vec3(0.0), vec3(0.0));
  foo += textureCubeGradARB(sc, vec3(0.0), vec3(0.0), vec3(0.0));
  foo += shadow1DGradARB(s1s, vec3(0.0), 0.0, 0.0);
  foo += shadow1DProjGradARB(s1s, vec4(0.0), 0.0, 0.0);
  foo += shadow2DGradARB(s2s, vec3(0.0), vec2(0.0), vec2(0.0));
  foo += shadow2DProjGradARB(s2s, vec4(0.0), vec2(0.0), vec2(0.0));
  gl_Position = foo;
  EmitVertex();
}
