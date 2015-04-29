// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS
 *
 * Based on the Regnum Online shader in bugzilla #28138.
 * */
#version 120

#line 336
#extension GL_ARB_texture_rectangle : enable
#pragma optionNV(fastmath on)
#pragma optionNV(fastprecision on)

varying vec4 clrAdd;
varying vec4 clrAddSmooth;
uniform float g_fBloomAddSmoothFactor;

void main()
{
  gl_FragColor.xyz = mix(clrAdd, clrAddSmooth, g_fBloomAddSmoothFactor).xyz;
  gl_FragColor.w = 1.0;
}
