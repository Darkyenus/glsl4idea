// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/*
 * GStreamer
 * Copyright (C) 2008 Cyril Comparon <cyril.comparon@gmail.com>
 * Copyright (C) 2008 Julien Isorce <julien.isorce@gmail.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

attribute vec3 aTangent;

varying vec3 vNormal;
varying vec3 vTangent;
varying vec3 vVertexToLight0;
varying vec3 vVertexToLight1;

void main()
{
  // transform the vertex
  gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * gl_Vertex;

  // transform the normal and the tangent to scene coords
  vNormal = normalize(gl_NormalMatrix * gl_Normal);
  vTangent = normalize(gl_NormalMatrix * aTangent);

  // transforming the vertex position to modelview-space
  //const vec4 vertexInSceneCoords = gl_ModelViewMatrix * gl_Vertex;

  // calculate the vector from the vertex position to the light position
  vVertexToLight0 = normalize(gl_LightSource[0].position).xyz;
  vVertexToLight1 = normalize(gl_LightSource[1].position).xyz;

  // transit vertex color
  gl_FrontColor = gl_BackColor = gl_Color;

  // use the two first sets of texture coordinates in the fragment shader
  gl_TexCoord[0] = gl_MultiTexCoord0;
  gl_TexCoord[1] = gl_MultiTexCoord1;
}
