// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/*
===========================================================================
Copyright (C) 2006 Robert Beckebans <trebor_7@users.sourceforge.net>

This file is part of XreaL source code.

XreaL source code is free software; you can redistribute it
and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

XreaL source code is distributed in the hope that it will be
useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with XreaL source code; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
===========================================================================
*/

uniform sampler2D	u_DiffuseMap;
uniform sampler2D	u_BumpMap;
uniform sampler2D	u_SpecularMap;
uniform sampler2D	u_AttenuationMapXY;
uniform sampler2D	u_AttenuationMapZ;
uniform vec3		u_ViewOrigin;
uniform vec3		u_LightOrigin;
uniform vec3		u_LightColor;
uniform float       u_LightScale;
uniform float		u_SpecularExponent;

varying vec3		var_Vertex;
varying vec2		var_TexDiffuse;
varying vec2		var_TexBump;
varying vec2		var_TexSpecular;
varying vec4		var_TexAtten;
varying mat3		var_OS2TSMatrix;

void	main()
{	
	// compute view direction in tangent space
	vec3 V = normalize(var_OS2TSMatrix * (u_ViewOrigin - var_Vertex));
	
	// compute light direction in tangent space
	vec3 L = normalize(var_OS2TSMatrix * (u_LightOrigin - var_Vertex));
	
	// compute half angle in tangent space
	vec3 H = normalize(L + V);
	
	// compute normal in tangent space from bumpmap
	vec3 N = 2.0 * (texture2D(u_BumpMap, var_TexBump).xyz - 0.5);
	N = normalize(N);
	
	// compute the diffuse term
	vec4 diffuse = texture2D(u_DiffuseMap, var_TexDiffuse);
	diffuse.rgb *= u_LightColor * clamp(dot(N, L), 0.0, 1.0);
	
	// compute the specular term
	vec3 specular = texture2D(u_SpecularMap, var_TexSpecular).rgb * u_LightColor * pow(clamp(dot(N, H), 0.0, 1.0), u_SpecularExponent);
	
	// compute attenuation
//	vec3 attenuationXY = texture2D(u_AttenuationMapXY, var_TexAtten.xy).rgb;
	vec3 attenuationXY = texture2DProj(u_AttenuationMapXY, vec3(var_TexAtten.x, var_TexAtten.y, var_TexAtten.w)).rgb;
	vec3 attenuationZ  = texture2D(u_AttenuationMapZ, vec2(var_TexAtten.z, 0)).rgb;
					
	// compute final color
	vec4 color = diffuse;
	color.rgb += specular;
	color.rgb *= attenuationXY;
	color.rgb *= attenuationZ;
	color.rgb *= u_LightScale;

	gl_FragColor = color;
}

