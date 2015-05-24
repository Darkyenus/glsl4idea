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

attribute vec4		attr_TexCoord0;
attribute vec3		attr_Tangent;
attribute vec3		attr_Binormal;

varying vec3		var_Vertex;
varying vec2		var_TexDiffuse;
varying vec2		var_TexBump;
varying vec4		var_TexAtten;
varying mat3		var_OS2TSMatrix;

void	main()
{
	// transform vertex position into homogenous clip-space
	gl_Position = ftransform();
	
	// assign position in object space
	var_Vertex = gl_Vertex.xyz;
		
	// transform diffusemap texcoords
	var_TexDiffuse = (gl_TextureMatrix[0] * attr_TexCoord0).st;
	
	// transform bumpmap texcoords
	var_TexBump = (gl_TextureMatrix[1] * attr_TexCoord0).st;
	
	// calc light xy,z attenuation in light space
	var_TexAtten = gl_TextureMatrix[3] * gl_Vertex;

	// construct object-space-to-tangent-space 3x3 matrix
	var_OS2TSMatrix = mat3(	attr_Tangent.x, attr_Binormal.x, gl_Normal.x,
							attr_Tangent.y, attr_Binormal.y, gl_Normal.y,
							attr_Tangent.z, attr_Binormal.z, gl_Normal.z	);
}
