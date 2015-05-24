// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
#version 110

uniform float u_float;
uniform vec2 u_vec2;
uniform vec3 u_vec3;
uniform vec4 u_vec4;

void main()
{
  gl_FragColor = gl_Color;

  float t_float = float(0.0);
  vec2  t_vec2  = vec2 (0.0);
  vec3  t_vec3  = vec3 (0.0);
  vec4  t_vec4  = vec4 (0.0);

  t_float = dFdx(u_float);
  t_vec2 = dFdx(u_vec2);
  t_vec3 = dFdx(u_vec3);
  t_vec4 = dFdx(u_vec4);
  t_float = dFdy(u_float);
  t_vec2 = dFdy(u_vec2);
  t_vec3 = dFdy(u_vec3);
  t_vec4 = dFdy(u_vec4);
  t_float = fwidth(u_float);
  t_vec2 = fwidth(u_vec2);
  t_vec3 = fwidth(u_vec3);
  t_vec4 = fwidth(u_vec4);
}
