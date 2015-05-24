/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_geometry_shader4 GL_ARB_texture_cube_map_array
 * [end config]
 *
 * Verify that GL_ARB_texture_cube_map_array can be used in geometry shaders
 */
#version 130
#extension GL_ARB_geometry_shader4: require
#extension GL_ARB_texture_cube_map_array: require

uniform samplerCubeArray sca;
uniform samplerCubeArrayShadow scas;
uniform isamplerCubeArray isca;
uniform usamplerCubeArray usca;

void main()
{
  vec4 foo = vec4(textureSize(sca, 0), 0.0);
  foo += vec4(textureSize(isca, 0), 0.0);
  foo += vec4(textureSize(usca, 0), 0.0);
  foo += texture(sca, vec4(0.0));
  foo += vec4(texture(isca, vec4(0.0)));
  foo += vec4(texture(usca, vec4(0.0)));
  foo += textureLod(sca, vec4(0.0), 0.0);
  foo += vec4(textureLod(isca, vec4(0.0), 0.0));
  foo += vec4(textureLod(usca, vec4(0.0), 0.0));
  foo += vec4(textureSize(scas, 0), 0.0);
  foo += vec4(texture(scas, vec4(0.0), 0.0));
  foo += textureGrad(sca, vec4(0.0), vec3(0.0), vec3(0.0));
  foo += vec4(textureGrad(isca, vec4(0.0), vec3(0.0), vec3(0.0)));
  foo += vec4(textureGrad(usca, vec4(0.0), vec3(0.0), vec3(0.0)));
  gl_Position = foo;
  EmitVertex();
}
