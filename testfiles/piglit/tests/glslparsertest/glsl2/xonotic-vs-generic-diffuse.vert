// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/*
Copyright (C) 1996-1997 Id Software, Inc.

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details.

You should have received a copy of the GNU General Public
License along with this program. If not, see
<http://www.gnu.org/licenses/>.


*/

/* Modification from original source: Pasted in #defines from the C
 * source by anholt.
 */
#define VERTEX_SHADER
#define MODE_GENERIC
#define USEDIFFUSE

/* Begin original shader source. */
// ambient+diffuse+specular+normalmap+attenuation+cubemap+fog shader
// written by Forest 'LordHavoc' Hale
// shadowmapping enhancements by Lee 'eihrul' Salzman

#if defined(USEFOGINSIDE) || defined(USEFOGOUTSIDE) || defined(USEFOGHEIGHTTEXTURE)
# define USEFOG
#endif
#if defined(MODE_LIGHTMAP) || defined(MODE_LIGHTDIRECTIONMAP_MODELSPACE) || defined(MODE_LIGHTDIRECTIONMAP_TANGENTSPACE)
#define USELIGHTMAP
#endif
#if defined(USESPECULAR) || defined(USEOFFSETMAPPING) || defined(USEREFLECTCUBE)
#define USEEYEVECTOR
#endif

#if defined(USESHADOWMAPRECT) || defined(MODE_DEFERREDLIGHTSOURCE) || defined(USEDEFERREDLIGHTMAP)
# extension GL_ARB_texture_rectangle : enable
#endif

#ifdef USESHADOWMAP2D
# ifdef GL_EXT_gpu_shader4
#   extension GL_EXT_gpu_shader4 : enable
# endif
# ifdef GL_ARB_texture_gather
#   extension GL_ARB_texture_gather : enable
# else
#   ifdef GL_AMD_texture_texture4
#     extension GL_AMD_texture_texture4 : enable
#   endif
# endif
#endif

#ifdef USESHADOWMAPCUBE
# extension GL_EXT_gpu_shader4 : enable
#endif

//#ifdef USESHADOWSAMPLER
//# extension GL_ARB_shadow : enable
//#endif

//#ifdef __GLSL_CG_DATA_TYPES
//# define myhalf half
//# define myhalf2 half2
//# define myhalf3 half3
//# define myhalf4 half4
//#else
# define myhalf float
# define myhalf2 vec2
# define myhalf3 vec3
# define myhalf4 vec4
//#endif

#ifdef VERTEX_SHADER
uniform mat4 ModelViewProjectionMatrix;
#endif

#ifdef MODE_DEPTH_OR_SHADOW
#ifdef VERTEX_SHADER
void main(void)
{
	gl_Position = ModelViewProjectionMatrix * gl_Vertex;
}
#endif
#else // !MODE_DEPTH_ORSHADOW




#ifdef MODE_SHOWDEPTH
#ifdef VERTEX_SHADER
void main(void)
{
	gl_Position = ModelViewProjectionMatrix * gl_Vertex;
	gl_FrontColor = vec4(gl_Position.z, gl_Position.z, gl_Position.z, 1.0);
}
#endif

#ifdef FRAGMENT_SHADER
void main(void)
{
	gl_FragColor = gl_Color;
}
#endif
#else // !MODE_SHOWDEPTH




#ifdef MODE_POSTPROCESS
varying vec2 TexCoord1;
varying vec2 TexCoord2;

#ifdef VERTEX_SHADER
void main(void)
{
	gl_Position = ModelViewProjectionMatrix * gl_Vertex;
	TexCoord1 = gl_MultiTexCoord0.xy;
#ifdef USEBLOOM
	TexCoord2 = gl_MultiTexCoord4.xy;
#endif
}
#endif

#ifdef FRAGMENT_SHADER
uniform sampler2D Texture_First;
#ifdef USEBLOOM
uniform sampler2D Texture_Second;
uniform vec4 BloomColorSubtract;
#endif
#ifdef USEGAMMARAMPS
uniform sampler2D Texture_GammaRamps;
#endif
#ifdef USESATURATION
uniform float Saturation;
#endif
#ifdef USEVIEWTINT
uniform vec4 ViewTintColor;
#endif
//uncomment these if you want to use them:
uniform vec4 UserVec1;
uniform vec4 UserVec2;
// uniform vec4 UserVec3;
// uniform vec4 UserVec4;
// uniform float ClientTime;
uniform vec2 PixelSize;
void main(void)
{
	gl_FragColor = texture2D(Texture_First, TexCoord1);
#ifdef USEBLOOM
	gl_FragColor += max(vec4(0,0,0,0), texture2D(Texture_Second, TexCoord2) - BloomColorSubtract);
#endif
#ifdef USEVIEWTINT
	gl_FragColor = mix(gl_FragColor, ViewTintColor, ViewTintColor.a);
#endif

#ifdef USEPOSTPROCESSING
// do r_glsl_dumpshader, edit glsl/default.glsl, and replace this by your own postprocessing if you want
// this code does a blur with the radius specified in the first component of r_glsl_postprocess_uservec1 and blends it using the second component
	float sobel = 1.0;
	// vec2 ts = textureSize(Texture_First, 0);
	// vec2 px = vec2(1/ts.x, 1/ts.y);
	vec2 px = PixelSize;
	vec3 x1 = texture2D(Texture_First, TexCoord1 + vec2(-px.x, px.y)).rgb;
	vec3 x2 = texture2D(Texture_First, TexCoord1 + vec2(-px.x,  0.0)).rgb;
	vec3 x3 = texture2D(Texture_First, TexCoord1 + vec2(-px.x,-px.y)).rgb;
	vec3 x4 = texture2D(Texture_First, TexCoord1 + vec2( px.x, px.y)).rgb;
	vec3 x5 = texture2D(Texture_First, TexCoord1 + vec2( px.x,  0.0)).rgb;
	vec3 x6 = texture2D(Texture_First, TexCoord1 + vec2( px.x,-px.y)).rgb;
	vec3 y1 = texture2D(Texture_First, TexCoord1 + vec2( px.x,-px.y)).rgb;
	vec3 y2 = texture2D(Texture_First, TexCoord1 + vec2(  0.0,-px.y)).rgb;
	vec3 y3 = texture2D(Texture_First, TexCoord1 + vec2(-px.x,-px.y)).rgb;
	vec3 y4 = texture2D(Texture_First, TexCoord1 + vec2( px.x, px.y)).rgb;
	vec3 y5 = texture2D(Texture_First, TexCoord1 + vec2(  0.0, px.y)).rgb;
	vec3 y6 = texture2D(Texture_First, TexCoord1 + vec2(-px.x, px.y)).rgb;
	float px1 = -1.0 * dot(vec3(0.3, 0.59, 0.11), x1);
	float px2 = -2.0 * dot(vec3(0.3, 0.59, 0.11), x2);
	float px3 = -1.0 * dot(vec3(0.3, 0.59, 0.11), x3);
	float px4 =  1.0 * dot(vec3(0.3, 0.59, 0.11), x4);
	float px5 =  2.0 * dot(vec3(0.3, 0.59, 0.11), x5);
	float px6 =  1.0 * dot(vec3(0.3, 0.59, 0.11), x6);
	float py1 = -1.0 * dot(vec3(0.3, 0.59, 0.11), y1);
	float py2 = -2.0 * dot(vec3(0.3, 0.59, 0.11), y2);
	float py3 = -1.0 * dot(vec3(0.3, 0.59, 0.11), y3);
	float py4 =  1.0 * dot(vec3(0.3, 0.59, 0.11), y4);
	float py5 =  2.0 * dot(vec3(0.3, 0.59, 0.11), y5);
	float py6 =  1.0 * dot(vec3(0.3, 0.59, 0.11), y6);
	sobel = 0.25 * abs(px1 + px2 + px3 + px4 + px5 + px6) + 0.25 * abs(py1 + py2 + py3 + py4 + py5 + py6);
	gl_FragColor += texture2D(Texture_First, TexCoord1 + PixelSize*UserVec1.x*vec2(-0.987688, -0.156434)) * UserVec1.y;
	gl_FragColor += texture2D(Texture_First, TexCoord1 + PixelSize*UserVec1.x*vec2(-0.156434, -0.891007)) * UserVec1.y;
	gl_FragColor += texture2D(Texture_First, TexCoord1 + PixelSize*UserVec1.x*vec2( 0.891007, -0.453990)) * UserVec1.y;
	gl_FragColor += texture2D(Texture_First, TexCoord1 + PixelSize*UserVec1.x*vec2( 0.707107,  0.707107)) * UserVec1.y;
	gl_FragColor += texture2D(Texture_First, TexCoord1 + PixelSize*UserVec1.x*vec2(-0.453990,  0.891007)) * UserVec1.y;
	gl_FragColor /= (1.0 + 5.0 * UserVec1.y);
	gl_FragColor.rgb = gl_FragColor.rgb * (1.0 + UserVec2.x) + vec3(max(0.0, sobel - UserVec2.z))*UserVec2.y;
#endif

#ifdef USESATURATION
	//apply saturation BEFORE gamma ramps, so v_glslgamma value does not matter
	float y = dot(gl_FragColor.rgb, vec3(0.299, 0.587, 0.114));
	//gl_FragColor = vec3(y) + (gl_FragColor.rgb - vec3(y)) * Saturation;
	gl_FragColor.rgb = mix(vec3(y), gl_FragColor.rgb, Saturation);
#endif

#ifdef USEGAMMARAMPS
	gl_FragColor.r = texture2D(Texture_GammaRamps, vec2(gl_FragColor.r, 0)).r;
	gl_FragColor.g = texture2D(Texture_GammaRamps, vec2(gl_FragColor.g, 0)).g;
	gl_FragColor.b = texture2D(Texture_GammaRamps, vec2(gl_FragColor.b, 0)).b;
#endif
}
#endif
#else // !MODE_POSTPROCESS




#ifdef MODE_GENERIC
#ifdef USEDIFFUSE
varying vec2 TexCoord1;
#endif
#ifdef USESPECULAR
varying vec2 TexCoord2;
#endif
#ifdef VERTEX_SHADER
void main(void)
{
	gl_FrontColor = gl_Color;
#ifdef USEDIFFUSE
	TexCoord1 = gl_MultiTexCoord0.xy;
#endif
#ifdef USESPECULAR
	TexCoord2 = gl_MultiTexCoord1.xy;
#endif
	gl_Position = ModelViewProjectionMatrix * gl_Vertex;
}
#endif

#ifdef FRAGMENT_SHADER
#ifdef USEDIFFUSE
uniform sampler2D Texture_First;
#endif
#ifdef USESPECULAR
uniform sampler2D Texture_Second;
#endif

void main(void)
{
	gl_FragColor = gl_Color;
#ifdef USEDIFFUSE
	gl_FragColor *= texture2D(Texture_First, TexCoord1);
#endif

#ifdef USESPECULAR
	vec4 tex2 = texture2D(Texture_Second, TexCoord2);
# ifdef USECOLORMAPPING
	gl_FragColor *= tex2;
# endif
# ifdef USEGLOW
	gl_FragColor += tex2;
# endif
# ifdef USEVERTEXTEXTUREBLEND
	gl_FragColor = mix(gl_FragColor, tex2, tex2.a);
# endif
#endif
}
#endif
#else // !MODE_GENERIC




#ifdef MODE_BLOOMBLUR
varying TexCoord;
#ifdef VERTEX_SHADER
void main(void)
{
	gl_FrontColor = gl_Color;
	TexCoord = gl_MultiTexCoord0.xy;
	gl_Position = ModelViewProjectionMatrix * gl_Vertex;
}
#endif

#ifdef FRAGMENT_SHADER
uniform sampler2D Texture_First;
uniform vec4 BloomBlur_Parameters;

void main(void)
{
	int i;
	vec2 tc = TexCoord;
	vec3 color = texture2D(Texture_First, tc).rgb;
	tc += BloomBlur_Parameters.xy;
	for (i = 1;i < SAMPLES;i++)
	{
		color += texture2D(Texture_First, tc).rgb;
		tc += BloomBlur_Parameters.xy;
	}
	gl_FragColor = vec4(color * BloomBlur_Parameters.z + vec3(BloomBlur_Parameters.w), 1);
}
#endif
#else // !MODE_BLOOMBLUR
#ifdef MODE_REFRACTION
varying vec2 TexCoord;
varying vec4 ModelViewProjectionPosition;
uniform mat4 TexMatrix;
#ifdef VERTEX_SHADER

void main(void)
{
	TexCoord = vec2(TexMatrix * gl_MultiTexCoord0);
	gl_Position = ModelViewProjectionMatrix * gl_Vertex;
	ModelViewProjectionPosition = gl_Position;
}
#endif

#ifdef FRAGMENT_SHADER
uniform sampler2D Texture_Normal;
uniform sampler2D Texture_Refraction;
uniform sampler2D Texture_Reflection;

uniform vec4 DistortScaleRefractReflect;
uniform vec4 ScreenScaleRefractReflect;
uniform vec4 ScreenCenterRefractReflect;
uniform vec4 RefractColor;
uniform vec4 ReflectColor;
uniform float ReflectFactor;
uniform float ReflectOffset;

void main(void)
{
	vec2 ScreenScaleRefractReflectIW = ScreenScaleRefractReflect.xy * (1.0 / ModelViewProjectionPosition.w);
	//vec2 ScreenTexCoord = (ModelViewProjectionPosition.xy + normalize(vec3(texture2D(Texture_Normal, TexCoord)) - vec3(0.5)).xy * DistortScaleRefractReflect.xy * 100) * ScreenScaleRefractReflectIW + ScreenCenterRefractReflect.xy;
	vec2 SafeScreenTexCoord = ModelViewProjectionPosition.xy * ScreenScaleRefractReflectIW + ScreenCenterRefractReflect.xy;
	vec2 ScreenTexCoord = SafeScreenTexCoord + vec2(normalize(vec3(texture2D(Texture_Normal, TexCoord)) - vec3(0.5))).xy * DistortScaleRefractReflect.xy;
	// FIXME temporary hack to detect the case that the reflection
	// gets blackened at edges due to leaving the area that contains actual
	// content.
	// Remove this 'ack once we have a better way to stop this thing from
	// 'appening.
	float f = min(1.0, length(texture2D(Texture_Refraction, ScreenTexCoord + vec2(0.01, 0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Refraction, ScreenTexCoord + vec2(0.01, -0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Refraction, ScreenTexCoord + vec2(-0.01, 0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Refraction, ScreenTexCoord + vec2(-0.01, -0.01)).rgb) / 0.05);
	ScreenTexCoord = mix(SafeScreenTexCoord, ScreenTexCoord, f);
	gl_FragColor = texture2D(Texture_Refraction, ScreenTexCoord) * RefractColor;
}
#endif
#else // !MODE_REFRACTION




#ifdef MODE_WATER
varying vec2 TexCoord;
varying vec3 EyeVector;
varying vec4 ModelViewProjectionPosition;
#ifdef VERTEX_SHADER
uniform vec3 EyePosition;
uniform mat4 TexMatrix;

void main(void)
{
	TexCoord = vec2(TexMatrix * gl_MultiTexCoord0);
	vec3 EyeVectorModelSpace = EyePosition - gl_Vertex.xyz;
	EyeVector.x = dot(EyeVectorModelSpace, gl_MultiTexCoord1.xyz);
	EyeVector.y = dot(EyeVectorModelSpace, gl_MultiTexCoord2.xyz);
	EyeVector.z = dot(EyeVectorModelSpace, gl_MultiTexCoord3.xyz);
	gl_Position = ModelViewProjectionMatrix * gl_Vertex;
	ModelViewProjectionPosition = gl_Position;
}
#endif

#ifdef FRAGMENT_SHADER
uniform sampler2D Texture_Normal;
uniform sampler2D Texture_Refraction;
uniform sampler2D Texture_Reflection;

uniform vec4 DistortScaleRefractReflect;
uniform vec4 ScreenScaleRefractReflect;
uniform vec4 ScreenCenterRefractReflect;
uniform vec4 RefractColor;
uniform vec4 ReflectColor;
uniform float ReflectFactor;
uniform float ReflectOffset;

void main(void)
{
	vec4 ScreenScaleRefractReflectIW = ScreenScaleRefractReflect * (1.0 / ModelViewProjectionPosition.w);
	//vec4 ScreenTexCoord = (ModelViewProjectionPosition.xyxy + normalize(vec3(texture2D(Texture_Normal, TexCoord)) - vec3(0.5)).xyxy * DistortScaleRefractReflect * 100) * ScreenScaleRefractReflectIW + ScreenCenterRefractReflect;
	vec4 SafeScreenTexCoord = ModelViewProjectionPosition.xyxy * ScreenScaleRefractReflectIW + ScreenCenterRefractReflect;
	//SafeScreenTexCoord = gl_FragCoord.xyxy * vec4(1.0 / 1920.0, 1.0 / 1200.0, 1.0 / 1920.0, 1.0 / 1200.0);
	vec4 ScreenTexCoord = SafeScreenTexCoord + vec2(normalize(vec3(texture2D(Texture_Normal, TexCoord)) - vec3(0.5))).xyxy * DistortScaleRefractReflect;
	// FIXME temporary hack to detect the case that the reflection
	// gets blackened at edges due to leaving the area that contains actual
	// content.
	// Remove this 'ack once we have a better way to stop this thing from
	// 'appening.
	float f = min(1.0, length(texture2D(Texture_Refraction, ScreenTexCoord.xy + vec2(0.01, 0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Refraction, ScreenTexCoord.xy + vec2(0.01, -0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Refraction, ScreenTexCoord.xy + vec2(-0.01, 0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Refraction, ScreenTexCoord.xy + vec2(-0.01, -0.01)).rgb) / 0.05);
	ScreenTexCoord.xy = mix(SafeScreenTexCoord.xy, ScreenTexCoord.xy, f);
	f       = min(1.0, length(texture2D(Texture_Reflection, ScreenTexCoord.zw + vec2(0.01, 0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Reflection, ScreenTexCoord.zw + vec2(0.01, -0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Reflection, ScreenTexCoord.zw + vec2(-0.01, 0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Reflection, ScreenTexCoord.zw + vec2(-0.01, -0.01)).rgb) / 0.05);
	ScreenTexCoord.zw = mix(SafeScreenTexCoord.zw, ScreenTexCoord.zw, f);
	float Fresnel = pow(min(1.0, 1.0 - float(normalize(EyeVector).z)), 2.0) * ReflectFactor + ReflectOffset;
	gl_FragColor = mix(texture2D(Texture_Refraction, ScreenTexCoord.xy) * RefractColor, texture2D(Texture_Reflection, ScreenTexCoord.zw) * ReflectColor, Fresnel);
}
#endif
#else // !MODE_WATER




// common definitions between vertex shader and fragment shader:

varying vec2 TexCoord;
#ifdef USEVERTEXTEXTUREBLEND
varying vec2 TexCoord2;
#endif
#ifdef USELIGHTMAP
varying vec2 TexCoordLightmap;
#endif

#ifdef MODE_LIGHTSOURCE
varying vec3 CubeVector;
#endif

#if (defined(MODE_LIGHTSOURCE) || defined(MODE_LIGHTDIRECTION)) && defined(USEDIFFUSE)
varying vec3 LightVector;
#endif

#ifdef USEEYEVECTOR
varying vec3 EyeVector;
#endif
#ifdef USEFOG
varying vec4 EyeVectorModelSpaceFogPlaneVertexDist;
#endif

#if defined(MODE_LIGHTDIRECTIONMAP_MODELSPACE) || defined(MODE_DEFERREDGEOMETRY) || defined(USEREFLECTCUBE)
varying vec3 VectorS; // direction of S texcoord (sometimes crudely called tangent)
varying vec3 VectorT; // direction of T texcoord (sometimes crudely called binormal)
varying vec3 VectorR; // direction of R texcoord (surface normal)
#endif

#ifdef USEREFLECTION
varying vec4 ModelViewProjectionPosition;
#endif
#ifdef MODE_DEFERREDLIGHTSOURCE
uniform vec3 LightPosition;
varying vec4 ModelViewPosition;
#endif

#ifdef MODE_LIGHTSOURCE
uniform vec3 LightPosition;
#endif
uniform vec3 EyePosition;
#ifdef MODE_LIGHTDIRECTION
uniform vec3 LightDir;
#endif
uniform vec4 FogPlane;

#ifdef USESHADOWMAPORTHO
varying vec3 ShadowMapTC;
#endif





// TODO: get rid of tangentt (texcoord2) and use a crossproduct to regenerate it from tangents (texcoord1) and normal (texcoord3), this would require sending a 4 component texcoord1 with W as 1 or -1 according to which side the texcoord2 should be on

// fragment shader specific:
#ifdef FRAGMENT_SHADER

uniform sampler2D Texture_Normal;
uniform sampler2D Texture_Color;
uniform sampler2D Texture_Gloss;
#ifdef USEGLOW
uniform sampler2D Texture_Glow;
#endif
#ifdef USEVERTEXTEXTUREBLEND
uniform sampler2D Texture_SecondaryNormal;
uniform sampler2D Texture_SecondaryColor;
uniform sampler2D Texture_SecondaryGloss;
#ifdef USEGLOW
uniform sampler2D Texture_SecondaryGlow;
#endif
#endif
#ifdef USECOLORMAPPING
uniform sampler2D Texture_Pants;
uniform sampler2D Texture_Shirt;
#endif
#ifdef USEFOG
#ifdef USEFOGHEIGHTTEXTURE
uniform sampler2D Texture_FogHeightTexture;
#endif
uniform sampler2D Texture_FogMask;
#endif
#ifdef USELIGHTMAP
uniform sampler2D Texture_Lightmap;
#endif
#if defined(MODE_LIGHTDIRECTIONMAP_MODELSPACE) || defined(MODE_LIGHTDIRECTIONMAP_TANGENTSPACE)
uniform sampler2D Texture_Deluxemap;
#endif
#ifdef USEREFLECTION
uniform sampler2D Texture_Reflection;
#endif

#ifdef MODE_DEFERREDLIGHTSOURCE
uniform sampler2D Texture_ScreenDepth;
uniform sampler2D Texture_ScreenNormalMap;
#endif
#ifdef USEDEFERREDLIGHTMAP
uniform sampler2D Texture_ScreenDiffuse;
uniform sampler2D Texture_ScreenSpecular;
#endif

uniform myhalf3 Color_Pants;
uniform myhalf3 Color_Shirt;
uniform myhalf3 FogColor;

#ifdef USEFOG
uniform float FogRangeRecip;
uniform float FogPlaneViewDist;
uniform float FogHeightFade;
vec3 FogVertex(vec3 surfacecolor)
{
	vec3 EyeVectorModelSpace = EyeVectorModelSpaceFogPlaneVertexDist.xyz;
	float FogPlaneVertexDist = EyeVectorModelSpaceFogPlaneVertexDist.w;
	float fogfrac;
#ifdef USEFOGHEIGHTTEXTURE
	vec4 fogheightpixel = texture2D(Texture_FogHeightTexture, vec2(1,1) + vec2(FogPlaneVertexDist, FogPlaneViewDist) * (-2.0 * FogHeightFade));
	fogfrac = fogheightpixel.a;
	return mix(fogheightpixel.rgb * FogColor, surfacecolor, texture2D(Texture_FogMask, myhalf2(length(EyeVectorModelSpace)*fogfrac*FogRangeRecip, 0.0)).r);
#else
# ifdef USEFOGOUTSIDE
	fogfrac = min(0.0, FogPlaneVertexDist) / (FogPlaneVertexDist - FogPlaneViewDist) * min(1.0, min(0.0, FogPlaneVertexDist) * FogHeightFade);
# else
	fogfrac = FogPlaneViewDist / (FogPlaneViewDist - max(0.0, FogPlaneVertexDist)) * min(1.0, (min(0.0, FogPlaneVertexDist) + FogPlaneViewDist) * FogHeightFade);
# endif
	return mix(FogColor, surfacecolor, texture2D(Texture_FogMask, myhalf2(length(EyeVectorModelSpace)*fogfrac*FogRangeRecip, 0.0)).r);
#endif
}
#endif

#ifdef USEOFFSETMAPPING
uniform float OffsetMapping_Scale;
vec2 OffsetMapping(vec2 TexCoord)
{
#ifdef USEOFFSETMAPPING_RELIEFMAPPING
	// 14 sample relief mapping: linear search and then binary search
	// this basically steps forward a small amount repeatedly until it finds
	// itself inside solid, then jitters forward and back using decreasing
	// amounts to find the impact
	//vec3 OffsetVector = vec3(EyeVector.xy * ((1.0 / EyeVector.z) * OffsetMapping_Scale) * vec2(-1, 1), -1);
	//vec3 OffsetVector = vec3(normalize(EyeVector.xy) * OffsetMapping_Scale * vec2(-1, 1), -1);
	vec3 OffsetVector = vec3(normalize(EyeVector).xy * OffsetMapping_Scale * vec2(-1, 1), -1);
	vec3 RT = vec3(TexCoord, 1);
	OffsetVector *= 0.1;
	RT += OffsetVector *  step(texture2D(Texture_Normal, RT.xy).a, RT.z);
	RT += OffsetVector *  step(texture2D(Texture_Normal, RT.xy).a, RT.z);
	RT += OffsetVector *  step(texture2D(Texture_Normal, RT.xy).a, RT.z);
	RT += OffsetVector *  step(texture2D(Texture_Normal, RT.xy).a, RT.z);
	RT += OffsetVector *  step(texture2D(Texture_Normal, RT.xy).a, RT.z);
	RT += OffsetVector *  step(texture2D(Texture_Normal, RT.xy).a, RT.z);
	RT += OffsetVector *  step(texture2D(Texture_Normal, RT.xy).a, RT.z);
	RT += OffsetVector *  step(texture2D(Texture_Normal, RT.xy).a, RT.z);
	RT += OffsetVector *  step(texture2D(Texture_Normal, RT.xy).a, RT.z);
	RT += OffsetVector * (step(texture2D(Texture_Normal, RT.xy).a, RT.z)          - 0.5);
	RT += OffsetVector * (step(texture2D(Texture_Normal, RT.xy).a, RT.z) * 0.5    - 0.25);
	RT += OffsetVector * (step(texture2D(Texture_Normal, RT.xy).a, RT.z) * 0.25   - 0.125);
	RT += OffsetVector * (step(texture2D(Texture_Normal, RT.xy).a, RT.z) * 0.125  - 0.0625);
	RT += OffsetVector * (step(texture2D(Texture_Normal, RT.xy).a, RT.z) * 0.0625 - 0.03125);
	return RT.xy;
#else
	// 3 sample offset mapping (only 3 samples because of ATI Radeon 9500-9800/X300 limits)
	// this basically moves forward the full distance, and then backs up based
	// on height of samples
	//vec2 OffsetVector = vec2(EyeVector.xy * ((1.0 / EyeVector.z) * OffsetMapping_Scale) * vec2(-1, 1));
	//vec2 OffsetVector = vec2(normalize(EyeVector.xy) * OffsetMapping_Scale * vec2(-1, 1));
	vec2 OffsetVector = vec2(normalize(EyeVector).xy * OffsetMapping_Scale * vec2(-1, 1));
	TexCoord += OffsetVector;
	OffsetVector *= 0.333;
	TexCoord -= OffsetVector * texture2D(Texture_Normal, TexCoord).a;
	TexCoord -= OffsetVector * texture2D(Texture_Normal, TexCoord).a;
	TexCoord -= OffsetVector * texture2D(Texture_Normal, TexCoord).a;
	return TexCoord;
#endif
}
#endif // USEOFFSETMAPPING

#if defined(MODE_LIGHTSOURCE) || defined(MODE_DEFERREDLIGHTSOURCE)
uniform sampler2D Texture_Attenuation;
uniform samplerCube Texture_Cube;
#endif

#if defined(MODE_LIGHTSOURCE) || defined(MODE_DEFERREDLIGHTSOURCE) || defined(USESHADOWMAPORTHO)

#ifdef USESHADOWMAPRECT
# ifdef USESHADOWSAMPLER
uniform sampler2DRectShadow Texture_ShadowMapRect;
# else
uniform sampler2DRect Texture_ShadowMapRect;
# endif
#endif

#ifdef USESHADOWMAP2D
# ifdef USESHADOWSAMPLER
uniform sampler2DShadow Texture_ShadowMap2D;
# else
uniform sampler2D Texture_ShadowMap2D;
# endif
#endif

#ifdef USESHADOWMAPVSDCT
uniform samplerCube Texture_CubeProjection;
#endif

#ifdef USESHADOWMAPCUBE
# ifdef USESHADOWSAMPLER
uniform samplerCubeShadow Texture_ShadowMapCube;
# else
uniform samplerCube Texture_ShadowMapCube;
# endif
#endif

#if defined(USESHADOWMAPRECT) || defined(USESHADOWMAP2D) || defined(USESHADOWMAPCUBE)
uniform vec2 ShadowMap_TextureScale;
uniform vec4 ShadowMap_Parameters;
#endif

#if defined(USESHADOWMAPRECT) || defined(USESHADOWMAP2D)
# ifdef USESHADOWMAPORTHO
#  define GetShadowMapTC2D(dir) (min(dir, ShadowMap_Parameters.xyz))
# else
#  ifdef USESHADOWMAPVSDCT
vec3 GetShadowMapTC2D(vec3 dir)
{
	vec3 adir = abs(dir);
	vec2 aparams = ShadowMap_Parameters.xy / max(max(adir.x, adir.y), adir.z);
	vec4 proj = textureCube(Texture_CubeProjection, dir);
	return vec3(mix(dir.xy, dir.zz, proj.xy) * aparams.x + proj.zw * ShadowMap_Parameters.z, aparams.y + ShadowMap_Parameters.w);
}
#  else
vec3 GetShadowMapTC2D(vec3 dir)
{
	vec3 adir = abs(dir);
	float ma = adir.z;
	vec4 proj = vec4(dir, 2.5);
	if (adir.x > ma) { ma = adir.x; proj = vec4(dir.zyx, 0.5); }
	if (adir.y > ma) { ma = adir.y; proj = vec4(dir.xzy, 1.5); }
	vec2 aparams = ShadowMap_Parameters.xy / ma;
	return vec3(proj.xy * aparams.x + vec2(proj.z < 0.0 ? 1.5 : 0.5, proj.w) * ShadowMap_Parameters.z, aparams.y + ShadowMap_Parameters.w);
}
#  endif
# endif
#endif // defined(USESHADOWMAPRECT) || defined(USESHADOWMAP2D)

#ifdef USESHADOWMAPCUBE
vec4 GetShadowMapTCCube(vec3 dir)
{
	vec3 adir = abs(dir);
	return vec4(dir, ShadowMap_Parameters.w + ShadowMap_Parameters.y / max(max(adir.x, adir.y), adir.z));
}
#endif

# ifdef USESHADOWMAPRECT
float ShadowMapCompare(vec3 dir)
{
	vec3 shadowmaptc = GetShadowMapTC2D(dir);
	float f;
#  ifdef USESHADOWSAMPLER

#    ifdef USESHADOWMAPPCF
#      define texval(x, y) shadow2DRect(Texture_ShadowMapRect, shadowmaptc + vec3(x, y, 0.0)).r
	f = dot(vec4(0.25), vec4(texval(-0.4, 1.0), texval(-1.0, -0.4), texval(0.4, -1.0), texval(1.0, 0.4)));
#    else
	f = shadow2DRect(Texture_ShadowMapRect, shadowmaptc).r;
#    endif

#  else

#    ifdef USESHADOWMAPPCF
#      if USESHADOWMAPPCF > 1
#        define texval(x, y) texture2DRect(Texture_ShadowMapRect, center + vec2(x, y)).r
	vec2 center = shadowmaptc.xy - 0.5, offset = fract(center);
	vec4 row1 = step(shadowmaptc.z, vec4(texval(-1.0, -1.0), texval( 0.0, -1.0), texval( 1.0, -1.0), texval( 2.0, -1.0)));
	vec4 row2 = step(shadowmaptc.z, vec4(texval(-1.0,  0.0), texval( 0.0,  0.0), texval( 1.0,  0.0), texval( 2.0,  0.0)));
	vec4 row3 = step(shadowmaptc.z, vec4(texval(-1.0,  1.0), texval( 0.0,  1.0), texval( 1.0,  1.0), texval( 2.0,  1.0)));
	vec4 row4 = step(shadowmaptc.z, vec4(texval(-1.0,  2.0), texval( 0.0,  2.0), texval( 1.0,  2.0), texval( 2.0,  2.0)));
	vec4 cols = row2 + row3 + mix(row1, row4, offset.y);
	f = dot(mix(cols.xyz, cols.yzw, offset.x), vec3(1.0/9.0));
#      else
#        define texval(x, y) texture2DRect(Texture_ShadowMapRect, shadowmaptc.xy + vec2(x, y)).r
	vec2 offset = fract(shadowmaptc.xy);
	vec3 row1 = step(shadowmaptc.z, vec3(texval(-1.0, -1.0), texval( 0.0, -1.0), texval( 1.0, -1.0)));
	vec3 row2 = step(shadowmaptc.z, vec3(texval(-1.0,  0.0), texval( 0.0,  0.0), texval( 1.0,  0.0)));
	vec3 row3 = step(shadowmaptc.z, vec3(texval(-1.0,  1.0), texval( 0.0,  1.0), texval( 1.0,  1.0)));
	vec3 cols = row2 + mix(row1, row3, offset.y);
	f = dot(mix(cols.xy, cols.yz, offset.x), vec2(0.25));
#      endif
#    else
	f = step(shadowmaptc.z, texture2DRect(Texture_ShadowMapRect, shadowmaptc.xy).r);
#    endif

#  endif
#  ifdef USESHADOWMAPORTHO
	return mix(ShadowMap_Parameters.w, 1.0, f);
#  else
	return f;
#  endif
}
# endif

# ifdef USESHADOWMAP2D
float ShadowMapCompare(vec3 dir)
{
	vec3 shadowmaptc = GetShadowMapTC2D(dir);
	float f;

#  ifdef USESHADOWSAMPLER
#    ifdef USESHADOWMAPPCF
#      define texval(x, y) shadow2D(Texture_ShadowMap2D, vec3(center + vec2(x, y)*ShadowMap_TextureScale, shadowmaptc.z)).r  
	vec2 center = shadowmaptc.xy*ShadowMap_TextureScale;
	f = dot(vec4(0.25), vec4(texval(-0.4, 1.0), texval(-1.0, -0.4), texval(0.4, -1.0), texval(1.0, 0.4)));
#    else
	f = shadow2D(Texture_ShadowMap2D, vec3(shadowmaptc.xy*ShadowMap_TextureScale, shadowmaptc.z)).r;
#    endif
#  else
#    ifdef USESHADOWMAPPCF
#     if defined(GL_ARB_texture_gather) || defined(GL_AMD_texture_texture4)
#      ifdef GL_ARB_texture_gather
#        define texval(x, y) textureGatherOffset(Texture_ShadowMap2D, center, ivec2(x, y))
#      else
#        define texval(x, y) texture4(Texture_ShadowMap2D, center + vec2(x, y)*ShadowMap_TextureScale)
#      endif
	vec2 offset = fract(shadowmaptc.xy - 0.5), center = (shadowmaptc.xy - offset)*ShadowMap_TextureScale;
#      if USESHADOWMAPPCF > 1
   vec4 group1 = step(shadowmaptc.z, texval(-2.0, -2.0));
   vec4 group2 = step(shadowmaptc.z, texval( 0.0, -2.0));
   vec4 group3 = step(shadowmaptc.z, texval( 2.0, -2.0));
   vec4 group4 = step(shadowmaptc.z, texval(-2.0,  0.0));
   vec4 group5 = step(shadowmaptc.z, texval( 0.0,  0.0));
   vec4 group6 = step(shadowmaptc.z, texval( 2.0,  0.0));
   vec4 group7 = step(shadowmaptc.z, texval(-2.0,  2.0));
   vec4 group8 = step(shadowmaptc.z, texval( 0.0,  2.0));
   vec4 group9 = step(shadowmaptc.z, texval( 2.0,  2.0));
	vec4 locols = vec4(group1.ab, group3.ab);
	vec4 hicols = vec4(group7.rg, group9.rg);
	locols.yz += group2.ab;
	hicols.yz += group8.rg;
	vec4 midcols = vec4(group1.rg, group3.rg) + vec4(group7.ab, group9.ab) +
				vec4(group4.rg, group6.rg) + vec4(group4.ab, group6.ab) +
				mix(locols, hicols, offset.y);
	vec4 cols = group5 + vec4(group2.rg, group8.ab);
	cols.xyz += mix(midcols.xyz, midcols.yzw, offset.x);
	f = dot(cols, vec4(1.0/25.0));
#      else
	vec4 group1 = step(shadowmaptc.z, texval(-1.0, -1.0));
	vec4 group2 = step(shadowmaptc.z, texval( 1.0, -1.0));
	vec4 group3 = step(shadowmaptc.z, texval(-1.0,  1.0));
	vec4 group4 = step(shadowmaptc.z, texval( 1.0,  1.0));
	vec4 cols = vec4(group1.rg, group2.rg) + vec4(group3.ab, group4.ab) +
				mix(vec4(group1.ab, group2.ab), vec4(group3.rg, group4.rg), offset.y);
	f = dot(mix(cols.xyz, cols.yzw, offset.x), vec3(1.0/9.0));
#      endif
#     else
#      ifdef GL_EXT_gpu_shader4
#        define texval(x, y) texture2DOffset(Texture_ShadowMap2D, center, ivec2(x, y)).r
#      else
#        define texval(x, y) texture2D(Texture_ShadowMap2D, center + vec2(x, y)*ShadowMap_TextureScale).r  
#      endif
#      if USESHADOWMAPPCF > 1
	vec2 center = shadowmaptc.xy - 0.5, offset = fract(center);
	center *= ShadowMap_TextureScale;
	vec4 row1 = step(shadowmaptc.z, vec4(texval(-1.0, -1.0), texval( 0.0, -1.0), texval( 1.0, -1.0), texval( 2.0, -1.0)));
	vec4 row2 = step(shadowmaptc.z, vec4(texval(-1.0,  0.0), texval( 0.0,  0.0), texval( 1.0,  0.0), texval( 2.0,  0.0)));
	vec4 row3 = step(shadowmaptc.z, vec4(texval(-1.0,  1.0), texval( 0.0,  1.0), texval( 1.0,  1.0), texval( 2.0,  1.0)));
	vec4 row4 = step(shadowmaptc.z, vec4(texval(-1.0,  2.0), texval( 0.0,  2.0), texval( 1.0,  2.0), texval( 2.0,  2.0)));
	vec4 cols = row2 + row3 + mix(row1, row4, offset.y);
	f = dot(mix(cols.xyz, cols.yzw, offset.x), vec3(1.0/9.0));
#      else
	vec2 center = shadowmaptc.xy*ShadowMap_TextureScale, offset = fract(shadowmaptc.xy);
	vec3 row1 = step(shadowmaptc.z, vec3(texval(-1.0, -1.0), texval( 0.0, -1.0), texval( 1.0, -1.0)));
	vec3 row2 = step(shadowmaptc.z, vec3(texval(-1.0,  0.0), texval( 0.0,  0.0), texval( 1.0,  0.0)));
	vec3 row3 = step(shadowmaptc.z, vec3(texval(-1.0,  1.0), texval( 0.0,  1.0), texval( 1.0,  1.0)));
	vec3 cols = row2 + mix(row1, row3, offset.y);
	f = dot(mix(cols.xy, cols.yz, offset.x), vec2(0.25));
#      endif
#     endif
#    else
	f = step(shadowmaptc.z, texture2D(Texture_ShadowMap2D, shadowmaptc.xy*ShadowMap_TextureScale).r);
#    endif
#  endif
#  ifdef USESHADOWMAPORTHO
	return mix(ShadowMap_Parameters.w, 1.0, f);
#  else
	return f;
#  endif
}
# endif

# ifdef USESHADOWMAPCUBE
float ShadowMapCompare(vec3 dir)
{
	// apply depth texture cubemap as light filter
	vec4 shadowmaptc = GetShadowMapTCCube(dir);
	float f;
#  ifdef USESHADOWSAMPLER
	f = shadowCube(Texture_ShadowMapCube, shadowmaptc).r;
#  else
	f = step(shadowmaptc.w, textureCube(Texture_ShadowMapCube, shadowmaptc.xyz).r);
#  endif
	return f;
}
# endif
#endif // !defined(MODE_LIGHTSOURCE) && !defined(MODE_DEFERREDLIGHTSOURCE) && !defined(USESHADOWMAPORTHO)
#endif // FRAGMENT_SHADER




#ifdef MODE_DEFERREDGEOMETRY
#ifdef VERTEX_SHADER
uniform mat4 TexMatrix;
#ifdef USEVERTEXTEXTUREBLEND
uniform mat4 BackgroundTexMatrix;
#endif
uniform mat4 ModelViewMatrix;
void main(void)
{
	TexCoord = vec2(TexMatrix * gl_MultiTexCoord0);
#ifdef USEVERTEXTEXTUREBLEND
	gl_FrontColor = gl_Color;
	TexCoord2 = vec2(BackgroundTexMatrix * gl_MultiTexCoord0);
#endif

	// transform unnormalized eye direction into tangent space
#ifdef USEOFFSETMAPPING
	vec3 EyeVectorModelSpace = EyePosition - gl_Vertex.xyz;
	EyeVector.x = dot(EyeVectorModelSpace, gl_MultiTexCoord1.xyz);
	EyeVector.y = dot(EyeVectorModelSpace, gl_MultiTexCoord2.xyz);
	EyeVector.z = dot(EyeVectorModelSpace, gl_MultiTexCoord3.xyz);
#endif

	VectorS = (ModelViewMatrix * vec4(gl_MultiTexCoord1.xyz, 0)).xyz;
	VectorT = (ModelViewMatrix * vec4(gl_MultiTexCoord2.xyz, 0)).xyz;
	VectorR = (ModelViewMatrix * vec4(gl_MultiTexCoord3.xyz, 0)).xyz;
	gl_Position = ModelViewProjectionMatrix * gl_Vertex;
}
#endif // VERTEX_SHADER

#ifdef FRAGMENT_SHADER
void main(void)
{
#ifdef USEOFFSETMAPPING
	// apply offsetmapping
	vec2 TexCoordOffset = OffsetMapping(TexCoord);
#define TexCoord TexCoordOffset
#endif

#ifdef USEALPHAKILL
	if (texture2D(Texture_Color, TexCoord).a < 0.5)
		discard;
#endif

#ifdef USEVERTEXTEXTUREBLEND
	float alpha = texture2D(Texture_Color, TexCoord).a;
	float terrainblend = clamp(float(gl_Color.a) * alpha * 2.0 - 0.5, float(0.0), float(1.0));
	//float terrainblend = min(float(gl_Color.a) * alpha * 2.0, float(1.0));
	//float terrainblend = float(gl_Color.a) * alpha > 0.5;
#endif

#ifdef USEVERTEXTEXTUREBLEND
	vec3 surfacenormal = mix(vec3(texture2D(Texture_SecondaryNormal, TexCoord2)), vec3(texture2D(Texture_Normal, TexCoord)), terrainblend) - vec3(0.5, 0.5, 0.5);
	float a = mix(texture2D(Texture_SecondaryGloss, TexCoord2).a, texture2D(Texture_Gloss, TexCoord).a, terrainblend);
#else
	vec3 surfacenormal = vec3(texture2D(Texture_Normal, TexCoord)) - vec3(0.5, 0.5, 0.5);
	float a = texture2D(Texture_Gloss, TexCoord).a;
#endif

	gl_FragColor = vec4(normalize(surfacenormal.x * VectorS + surfacenormal.y * VectorT + surfacenormal.z * VectorR) * 0.5 + vec3(0.5, 0.5, 0.5), a);
}
#endif // FRAGMENT_SHADER
#else // !MODE_DEFERREDGEOMETRY




#ifdef MODE_DEFERREDLIGHTSOURCE
#ifdef VERTEX_SHADER
uniform mat4 ModelViewMatrix;
void main(void)
{
	ModelViewPosition = ModelViewMatrix * gl_Vertex;
	gl_Position = ModelViewProjectionMatrix * gl_Vertex;
}
#endif // VERTEX_SHADER

#ifdef FRAGMENT_SHADER
uniform mat4 ViewToLight;
// ScreenToDepth = vec2(Far / (Far - Near), Far * Near / (Near - Far));
uniform vec2 ScreenToDepth;
uniform myhalf3 DeferredColor_Ambient;
uniform myhalf3 DeferredColor_Diffuse;
#ifdef USESPECULAR
uniform myhalf3 DeferredColor_Specular;
uniform myhalf SpecularPower;
#endif
uniform myhalf2 PixelToScreenTexCoord;
void main(void)
{
	// calculate viewspace pixel position
	vec2 ScreenTexCoord = gl_FragCoord.xy * PixelToScreenTexCoord;
	vec3 position;
	position.z = ScreenToDepth.y / (texture2D(Texture_ScreenDepth, ScreenTexCoord).r + ScreenToDepth.x);
	position.xy = ModelViewPosition.xy * (position.z / ModelViewPosition.z);
	// decode viewspace pixel normal
	myhalf4 normalmap = texture2D(Texture_ScreenNormalMap, ScreenTexCoord);
	myhalf3 surfacenormal = normalize(normalmap.rgb - myhalf3(0.5,0.5,0.5));
	// surfacenormal = pixel normal in viewspace
	// LightVector = pixel to light in viewspace
	// CubeVector = position in lightspace
	// eyevector = pixel to view in viewspace
	vec3 CubeVector = vec3(ViewToLight * vec4(position,1));
	myhalf fade = myhalf(texture2D(Texture_Attenuation, vec2(length(CubeVector), 0.0)));
#ifdef USEDIFFUSE
	// calculate diffuse shading
	myhalf3 lightnormal = myhalf3(normalize(LightPosition - position));
	myhalf diffuse = myhalf(max(float(dot(surfacenormal, lightnormal)), 0.0));
#endif
#ifdef USESPECULAR
	// calculate directional shading
	vec3 eyevector = position * -1.0;
#  ifdef USEEXACTSPECULARMATH
	myhalf specular = pow(myhalf(max(float(dot(reflect(lightnormal, surfacenormal), normalize(eyevector)))*-1.0, 0.0)), SpecularPower * normalmap.a);
#  else
	myhalf3 specularnormal = normalize(lightnormal + myhalf3(normalize(eyevector)));
	myhalf specular = pow(myhalf(max(float(dot(surfacenormal, specularnormal)), 0.0)), SpecularPower * normalmap.a);
#  endif
#endif

#if defined(USESHADOWMAPRECT) || defined(USESHADOWMAPCUBE) || defined(USESHADOWMAP2D)
	fade *= ShadowMapCompare(CubeVector);
#endif

#ifdef USEDIFFUSE
	gl_FragData[0] = vec4((DeferredColor_Ambient + DeferredColor_Diffuse * diffuse) * fade, 1.0);
#else
	gl_FragData[0] = vec4(DeferredColor_Ambient * fade, 1.0);
#endif
#ifdef USESPECULAR
	gl_FragData[1] = vec4(DeferredColor_Specular * (specular * fade), 1.0);
#else
	gl_FragData[1] = vec4(0.0, 0.0, 0.0, 1.0);
#endif

# ifdef USECUBEFILTER
	vec3 cubecolor = textureCube(Texture_Cube, CubeVector).rgb;
	gl_FragData[0].rgb *= cubecolor;
	gl_FragData[1].rgb *= cubecolor;
# endif
}
#endif // FRAGMENT_SHADER
#else // !MODE_DEFERREDLIGHTSOURCE




#ifdef VERTEX_SHADER
uniform mat4 TexMatrix;
#ifdef USEVERTEXTEXTUREBLEND
uniform mat4 BackgroundTexMatrix;
#endif
#ifdef MODE_LIGHTSOURCE
uniform mat4 ModelToLight;
#endif
#ifdef USESHADOWMAPORTHO
uniform mat4 ShadowMapMatrix;
#endif
void main(void)
{
#if defined(MODE_VERTEXCOLOR) || defined(USEVERTEXTEXTUREBLEND)
	gl_FrontColor = gl_Color;
#endif
	// copy the surface texcoord
	TexCoord = vec2(TexMatrix * gl_MultiTexCoord0);
#ifdef USEVERTEXTEXTUREBLEND
	TexCoord2 = vec2(BackgroundTexMatrix * gl_MultiTexCoord0);
#endif
#ifdef USELIGHTMAP
	TexCoordLightmap = vec2(gl_MultiTexCoord4);
#endif

#ifdef MODE_LIGHTSOURCE
	// transform vertex position into light attenuation/cubemap space
	// (-1 to +1 across the light box)
	CubeVector = vec3(ModelToLight * gl_Vertex);

# ifdef USEDIFFUSE
	// transform unnormalized light direction into tangent space
	// (we use unnormalized to ensure that it interpolates correctly and then
	//  normalize it per pixel)
	vec3 lightminusvertex = LightPosition - gl_Vertex.xyz;
	LightVector.x = dot(lightminusvertex, gl_MultiTexCoord1.xyz);
	LightVector.y = dot(lightminusvertex, gl_MultiTexCoord2.xyz);
	LightVector.z = dot(lightminusvertex, gl_MultiTexCoord3.xyz);
# endif
#endif

#if defined(MODE_LIGHTDIRECTION) && defined(USEDIFFUSE)
	LightVector.x = dot(LightDir, gl_MultiTexCoord1.xyz);
	LightVector.y = dot(LightDir, gl_MultiTexCoord2.xyz);
	LightVector.z = dot(LightDir, gl_MultiTexCoord3.xyz);
#endif

	// transform unnormalized eye direction into tangent space
#ifdef USEEYEVECTOR
	vec3 EyeVectorModelSpace = EyePosition - gl_Vertex.xyz;
	EyeVector.x = dot(EyeVectorModelSpace, gl_MultiTexCoord1.xyz);
	EyeVector.y = dot(EyeVectorModelSpace, gl_MultiTexCoord2.xyz);
	EyeVector.z = dot(EyeVectorModelSpace, gl_MultiTexCoord3.xyz);
#endif

#ifdef USEFOG
	EyeVectorModelSpaceFogPlaneVertexDist.xyz = EyePosition - gl_Vertex.xyz;
	EyeVectorModelSpaceFogPlaneVertexDist.w = dot(FogPlane, gl_Vertex);
#endif

#if defined(MODE_LIGHTDIRECTIONMAP_MODELSPACE) || defined(USEREFLECTCUBE)
	VectorS = gl_MultiTexCoord1.xyz;
	VectorT = gl_MultiTexCoord2.xyz;
	VectorR = gl_MultiTexCoord3.xyz;
#endif

	// transform vertex to camera space, using ftransform to match non-VS rendering
	gl_Position = ModelViewProjectionMatrix * gl_Vertex;

#ifdef USESHADOWMAPORTHO
	ShadowMapTC = vec3(ShadowMapMatrix * gl_Position);
#endif

#ifdef USEREFLECTION
	ModelViewProjectionPosition = gl_Position;
#endif
}
#endif // VERTEX_SHADER




#ifdef FRAGMENT_SHADER
#ifdef USEDEFERREDLIGHTMAP
uniform myhalf2 PixelToScreenTexCoord;
uniform myhalf3 DeferredMod_Diffuse;
uniform myhalf3 DeferredMod_Specular;
#endif
uniform myhalf3 Color_Ambient;
uniform myhalf3 Color_Diffuse;
uniform myhalf3 Color_Specular;
uniform myhalf SpecularPower;
#ifdef USEGLOW
uniform myhalf3 Color_Glow;
#endif
uniform myhalf Alpha;
#ifdef USEREFLECTION
uniform vec4 DistortScaleRefractReflect;
uniform vec4 ScreenScaleRefractReflect;
uniform vec4 ScreenCenterRefractReflect;
uniform myhalf4 ReflectColor;
#endif
#ifdef USEREFLECTCUBE
uniform mat4 ModelToReflectCube;
uniform sampler2D Texture_ReflectMask;
uniform samplerCube Texture_ReflectCube;
#endif
#ifdef MODE_LIGHTDIRECTION
uniform myhalf3 LightColor;
#endif
#ifdef MODE_LIGHTSOURCE
uniform myhalf3 LightColor;
#endif
void main(void)
{
#ifdef USEOFFSETMAPPING
	// apply offsetmapping
	vec2 TexCoordOffset = OffsetMapping(TexCoord);
#define TexCoord TexCoordOffset
#endif

	// combine the diffuse textures (base, pants, shirt)
	myhalf4 color = myhalf4(texture2D(Texture_Color, TexCoord));
#ifdef USEALPHAKILL
	if (color.a < 0.5)
		discard;
#endif
	color.a *= Alpha;
#ifdef USECOLORMAPPING
	color.rgb += myhalf3(texture2D(Texture_Pants, TexCoord)) * Color_Pants + myhalf3(texture2D(Texture_Shirt, TexCoord)) * Color_Shirt;
#endif
#ifdef USEVERTEXTEXTUREBLEND
	myhalf terrainblend = clamp(myhalf(gl_Color.a) * color.a * 2.0 - 0.5, myhalf(0.0), myhalf(1.0));
	//myhalf terrainblend = min(myhalf(gl_Color.a) * color.a * 2.0, myhalf(1.0));
	//myhalf terrainblend = myhalf(gl_Color.a) * color.a > 0.5;
	color.rgb = mix(myhalf3(texture2D(Texture_SecondaryColor, TexCoord2)), color.rgb, terrainblend);
	color.a = 1.0;
	//color = mix(myhalf4(1, 0, 0, 1), color, terrainblend);
#endif

	// get the surface normal
#ifdef USEVERTEXTEXTUREBLEND
	myhalf3 surfacenormal = normalize(mix(myhalf3(texture2D(Texture_SecondaryNormal, TexCoord2)), myhalf3(texture2D(Texture_Normal, TexCoord)), terrainblend) - myhalf3(0.5, 0.5, 0.5));
#else
	myhalf3 surfacenormal = normalize(myhalf3(texture2D(Texture_Normal, TexCoord)) - myhalf3(0.5, 0.5, 0.5));
#endif

	// get the material colors
	myhalf3 diffusetex = color.rgb;
#if defined(USESPECULAR) || defined(USEDEFERREDLIGHTMAP)
# ifdef USEVERTEXTEXTUREBLEND
	myhalf4 glosstex = mix(myhalf4(texture2D(Texture_SecondaryGloss, TexCoord2)), myhalf4(texture2D(Texture_Gloss, TexCoord)), terrainblend);
# else
	myhalf4 glosstex = myhalf4(texture2D(Texture_Gloss, TexCoord));
# endif
#endif

#ifdef USEREFLECTCUBE
	vec3 TangentReflectVector = reflect(-EyeVector, surfacenormal);
	vec3 ModelReflectVector = TangentReflectVector.x * VectorS + TangentReflectVector.y * VectorT + TangentReflectVector.z * VectorR;
	vec3 ReflectCubeTexCoord = vec3(ModelToReflectCube * vec4(ModelReflectVector, 0));
	diffusetex += myhalf3(texture2D(Texture_ReflectMask, TexCoord)) * myhalf3(textureCube(Texture_ReflectCube, ReflectCubeTexCoord));
#endif




#ifdef MODE_LIGHTSOURCE
	// light source
#ifdef USEDIFFUSE
	myhalf3 lightnormal = myhalf3(normalize(LightVector));
	myhalf diffuse = myhalf(max(float(dot(surfacenormal, lightnormal)), 0.0));
	color.rgb = diffusetex * (Color_Ambient + diffuse * Color_Diffuse);
#ifdef USESPECULAR
#ifdef USEEXACTSPECULARMATH
	myhalf specular = pow(myhalf(max(float(dot(reflect(lightnormal, surfacenormal), normalize(EyeVector)))*-1.0, 0.0)), SpecularPower * glosstex.a);
#else
	myhalf3 specularnormal = normalize(lightnormal + myhalf3(normalize(EyeVector)));
	myhalf specular = pow(myhalf(max(float(dot(surfacenormal, specularnormal)), 0.0)), SpecularPower * glosstex.a);
#endif
	color.rgb += glosstex.rgb * (specular * Color_Specular);
#endif
#else
	color.rgb = diffusetex * Color_Ambient;
#endif
	color.rgb *= LightColor;
	color.rgb *= myhalf(texture2D(Texture_Attenuation, vec2(length(CubeVector), 0.0)));
#if defined(USESHADOWMAPRECT) || defined(USESHADOWMAPCUBE) || defined(USESHADOWMAP2D)
	color.rgb *= ShadowMapCompare(CubeVector);
#endif
# ifdef USECUBEFILTER
	color.rgb *= myhalf3(textureCube(Texture_Cube, CubeVector));
# endif
#endif // MODE_LIGHTSOURCE




#ifdef MODE_LIGHTDIRECTION
#define SHADING
#ifdef USEDIFFUSE
	myhalf3 lightnormal = myhalf3(normalize(LightVector));
#endif
#define lightcolor LightColor
#endif // MODE_LIGHTDIRECTION
#ifdef MODE_LIGHTDIRECTIONMAP_MODELSPACE
#define SHADING
	// deluxemap lightmapping using light vectors in modelspace (q3map2 -light -deluxe)
	myhalf3 lightnormal_modelspace = myhalf3(texture2D(Texture_Deluxemap, TexCoordLightmap)) * 2.0 + myhalf3(-1.0, -1.0, -1.0);
	myhalf3 lightcolor = myhalf3(texture2D(Texture_Lightmap, TexCoordLightmap));
	// convert modelspace light vector to tangentspace
	myhalf3 lightnormal;
	lightnormal.x = dot(lightnormal_modelspace, myhalf3(VectorS));
	lightnormal.y = dot(lightnormal_modelspace, myhalf3(VectorT));
	lightnormal.z = dot(lightnormal_modelspace, myhalf3(VectorR));
	// calculate directional shading (and undoing the existing angle attenuation on the lightmap by the division)
	// note that q3map2 is too stupid to calculate proper surface normals when q3map_nonplanar
	// is used (the lightmap and deluxemap coords correspond to virtually random coordinates
	// on that luxel, and NOT to its center, because recursive triangle subdivision is used
	// to map the luxels to coordinates on the draw surfaces), which also causes
	// deluxemaps to be wrong because light contributions from the wrong side of the surface
	// are added up. To prevent divisions by zero or strong exaggerations, a max()
	// nudge is done here at expense of some additional fps. This is ONLY needed for
	// deluxemaps, tangentspace deluxemap avoid this problem by design.
	lightcolor *= 1.0 / max(0.25, lightnormal.z);
#endif // MODE_LIGHTDIRECTIONMAP_MODELSPACE
#ifdef MODE_LIGHTDIRECTIONMAP_TANGENTSPACE
#define SHADING
	// deluxemap lightmapping using light vectors in tangentspace (hmap2 -light)
	myhalf3 lightnormal = myhalf3(texture2D(Texture_Deluxemap, TexCoordLightmap)) * 2.0 + myhalf3(-1.0, -1.0, -1.0);
	myhalf3 lightcolor = myhalf3(texture2D(Texture_Lightmap, TexCoordLightmap));
#endif




#ifdef MODE_LIGHTMAP
	color.rgb = diffusetex * (Color_Ambient + myhalf3(texture2D(Texture_Lightmap, TexCoordLightmap)) * Color_Diffuse);
#endif // MODE_LIGHTMAP
#ifdef MODE_VERTEXCOLOR
	color.rgb = diffusetex * (Color_Ambient + myhalf3(gl_Color.rgb) * Color_Diffuse);
#endif // MODE_VERTEXCOLOR
#ifdef MODE_FLATCOLOR
	color.rgb = diffusetex * Color_Ambient;
#endif // MODE_FLATCOLOR




#ifdef SHADING
# ifdef USEDIFFUSE
	myhalf diffuse = myhalf(max(float(dot(surfacenormal, lightnormal)), 0.0));
#  ifdef USESPECULAR
#   ifdef USEEXACTSPECULARMATH
	myhalf specular = pow(myhalf(max(float(dot(reflect(lightnormal, surfacenormal), normalize(EyeVector)))*-1.0, 0.0)), SpecularPower * glosstex.a);
#   else
	myhalf3 specularnormal = normalize(lightnormal + myhalf3(normalize(EyeVector)));
	myhalf specular = pow(myhalf(max(float(dot(surfacenormal, specularnormal)), 0.0)), SpecularPower * glosstex.a);
#   endif
	color.rgb = diffusetex * Color_Ambient + (diffusetex * Color_Diffuse * diffuse + glosstex.rgb * Color_Specular * specular) * lightcolor;
#  else
	color.rgb = diffusetex * (Color_Ambient + Color_Diffuse * diffuse * lightcolor);
#  endif
# else
	color.rgb = diffusetex * Color_Ambient;
# endif
#endif

#ifdef USESHADOWMAPORTHO
	color.rgb *= ShadowMapCompare(ShadowMapTC);
#endif

#ifdef USEDEFERREDLIGHTMAP
	vec2 ScreenTexCoord = gl_FragCoord.xy * PixelToScreenTexCoord;
	color.rgb += diffusetex * myhalf3(texture2D(Texture_ScreenDiffuse, ScreenTexCoord)) * DeferredMod_Diffuse;
	color.rgb += glosstex.rgb * myhalf3(texture2D(Texture_ScreenSpecular, ScreenTexCoord)) * DeferredMod_Specular;
#endif

#ifdef USEGLOW
#ifdef USEVERTEXTEXTUREBLEND
	color.rgb += mix(myhalf3(texture2D(Texture_SecondaryGlow, TexCoord2)), myhalf3(texture2D(Texture_Glow, TexCoord)), terrainblend) * Color_Glow;
#else
	color.rgb += myhalf3(texture2D(Texture_Glow, TexCoord)) * Color_Glow;
#endif
#endif

#ifdef USEFOG
	color.rgb = FogVertex(color.rgb);
#endif

	// reflection must come last because it already contains exactly the correct fog (the reflection render preserves camera distance from the plane, it only flips the side) and ContrastBoost/SceneBrightness
#ifdef USEREFLECTION
	vec4 ScreenScaleRefractReflectIW = ScreenScaleRefractReflect * (1.0 / ModelViewProjectionPosition.w);
	//vec4 ScreenTexCoord = (ModelViewProjectionPosition.xyxy + normalize(myhalf3(texture2D(Texture_Normal, TexCoord)) - myhalf3(0.5)).xyxy * DistortScaleRefractReflect * 100) * ScreenScaleRefractReflectIW + ScreenCenterRefractReflect;
	vec2 SafeScreenTexCoord = ModelViewProjectionPosition.xy * ScreenScaleRefractReflectIW.zw + ScreenCenterRefractReflect.zw;
	vec2 ScreenTexCoord = SafeScreenTexCoord + vec3(normalize(myhalf3(texture2D(Texture_Normal, TexCoord)) - myhalf3(0.5))).xy * DistortScaleRefractReflect.zw;
	// FIXME temporary hack to detect the case that the reflection
	// gets blackened at edges due to leaving the area that contains actual
	// content.
	// Remove this 'ack once we have a better way to stop this thing from
	// 'appening.
	float f = min(1.0, length(texture2D(Texture_Reflection, ScreenTexCoord + vec2(0.01, 0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Reflection, ScreenTexCoord + vec2(0.01, -0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Reflection, ScreenTexCoord + vec2(-0.01, 0.01)).rgb) / 0.05);
	f      *= min(1.0, length(texture2D(Texture_Reflection, ScreenTexCoord + vec2(-0.01, -0.01)).rgb) / 0.05);
	ScreenTexCoord = mix(SafeScreenTexCoord, ScreenTexCoord, f);
	color.rgb = mix(color.rgb, myhalf3(texture2D(Texture_Reflection, ScreenTexCoord)) * ReflectColor.rgb, ReflectColor.a);
#endif

	gl_FragColor = vec4(color);
}
#endif // FRAGMENT_SHADER

#endif // !MODE_DEFERREDLIGHTSOURCE
#endif // !MODE_DEFERREDGEOMETRY
#endif // !MODE_WATER
#endif // !MODE_REFRACTION
#endif // !MODE_BLOOMBLUR
#endif // !MODE_GENERIC
#endif // !MODE_POSTPROCESS
#endif // !MODE_SHOWDEPTH
#endif // !MODE_DEPTH_OR_SHADOW
