/* [config]
 * expect_result: pass
 * glsl_version: 1.50
 * require_extensions: GL_EXT_texture_array
 * [end config]
 *
 * Verify that GL_EXT_texture_array can be used in geometry shaders
 */
#version 150
#extension GL_EXT_texture_array: require

uniform sampler1DArray s1;
uniform sampler2DArray s2;
uniform sampler1DArrayShadow s1s;
uniform sampler2DArrayShadow s2s;

void main()
{
  vec4 foo = texture1DArray(s1, vec2(0.0));
  foo += texture1DArrayLod(s1, vec2(0.0), 0.0);
  foo += texture2DArray(s2, vec3(0.0));
  foo += texture2DArrayLod(s2, vec3(0.0), 0.0);
  foo += shadow1DArray(s1s, vec3(0.0));
  foo += shadow1DArrayLod(s1s, vec3(0.0), 0.0);
  foo += shadow2DArray(s2s, vec4(0.0));
  gl_Position = foo;
  EmitVertex();
}
