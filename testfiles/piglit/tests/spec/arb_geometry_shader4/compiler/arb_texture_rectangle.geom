/* [config]
 * expect_result: pass
 * glsl_version: 1.10
 * require_extensions: GL_ARB_geometry_shader4 GL_ARB_texture_rectangle
 * [end config]
 *
 * Verify that GL_ARB_texture_rectangle can be used in geometry shaders
 */
#extension GL_ARB_geometry_shader4: require
#extension GL_ARB_texture_rectangle: require

uniform sampler2DRect s;
uniform sampler2DRectShadow s_shadow;

void main()
{
  vec4 foo = texture2DRect(s, vec2(0.0));
  foo += texture2DRectProj(s, vec3(0.0));
  foo += texture2DRectProj(s, vec4(0.0));
  foo += shadow2DRect(s_shadow, vec3(0.0));
  foo += shadow2DRectProj(s_shadow, vec4(0.0));
  gl_Position = foo;
  EmitVertex();
}
