// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/*
 * bumpTBN_SH_VP.glsl - transforming to tangent space and computing shadow
 * mapping matrix
 *
 * Simple test GLSL shader
 * Copyright (c) 2007 Cesare Tirabassi <norsetto@ubuntu.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

varying vec3 lvec, vvec;
attribute vec3 tangent, binormal;

void main()
{
	gl_Position = ftransform();

	gl_TexCoord[0] = gl_MultiTexCoord0;

	gl_TexCoord[1] = gl_TextureMatrix[2] * gl_ModelViewMatrix * gl_Vertex;

	mat3 TBN = mat3(tangent, binormal, gl_Normal);

	lvec = (gl_ModelViewMatrixInverse * gl_LightSource[0].position - gl_Vertex).xyz;
	lvec *= TBN;

	vvec = (gl_ModelViewMatrixInverse[3] - gl_Vertex).xyz;
	vvec *= TBN;
}
