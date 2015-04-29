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

varying vec3		var_Vertex;
varying vec3        var_Normal;
varying vec2		var_TexDiffuse;
varying vec4		var_TexAtten;

void	main()
{
	// transform vertex position into homogenous clip-space
	gl_Position = ftransform();
	
	// assign position in object space
	var_Vertex = gl_Vertex.xyz;
	
	// assign normal in object space
	var_Normal = gl_Normal.xyz;
		
	// transform diffusemap texcoords
	var_TexDiffuse = (gl_TextureMatrix[0] * attr_TexCoord0).st;
	
	// calc light xy,z attenuation in light space
	var_TexAtten = gl_TextureMatrix[3] * gl_Vertex;
}
