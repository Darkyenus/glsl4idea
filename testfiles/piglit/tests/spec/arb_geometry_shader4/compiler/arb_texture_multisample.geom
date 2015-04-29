/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_geometry_shader4 GL_ARB_texture_multisample
 * [end config]
 *
 * Verify that GL_ARB_texture_multisample can be used in geometry shaders
 */
#version 130
#extension GL_ARB_geometry_shader4: require
#extension GL_ARB_texture_multisample: require

uniform sampler2DMS s;
uniform isampler2DMS is;
uniform usampler2DMS us;
uniform sampler2DMSArray sa;
uniform isampler2DMSArray isa;
uniform usampler2DMSArray usa;

void main()
{
  vec4 foo = texelFetch(s, ivec2(0, 0), 0);
  foo += vec4(texelFetch(is, ivec2(0, 0), 0));
  foo += vec4(texelFetch(us, ivec2(0, 0), 0));
  foo += texelFetch(sa, ivec3(0, 0, 0), 0);
  foo += vec4(texelFetch(isa, ivec3(0, 0, 0), 0));
  foo += vec4(texelFetch(usa, ivec3(0, 0, 0), 0));
  gl_Position = foo;
  EmitVertex();
}
