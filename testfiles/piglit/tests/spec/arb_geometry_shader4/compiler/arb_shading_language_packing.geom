/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_geometry_shader4 GL_ARB_shading_language_packing
 * [end config]
 *
 * Verify that GL_ARB_shading_language_packing can be used in geometry shaders
 */
#version 130
#extension GL_ARB_geometry_shader4: require
#extension GL_ARB_shading_language_packing: require

uniform vec2 v2;
uniform vec4 v4;
uniform uint u;

void main()
{
  vec4 foo = vec4(packUnorm2x16(v2));
  foo += vec4(packSnorm2x16(v2));
  foo += vec4(packUnorm4x8(v4));
  foo += vec4(packSnorm4x8(v4));
  foo += vec4(unpackUnorm2x16(u), 0.0, 0.0);
  foo += vec4(unpackSnorm2x16(u), 0.0, 0.0);
  foo += unpackUnorm4x8(u);
  foo += unpackSnorm4x8(u);
  gl_Position = foo;
  EmitVertex();
}
