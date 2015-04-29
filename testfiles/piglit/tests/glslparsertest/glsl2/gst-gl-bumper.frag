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

#extension GL_ARB_texture_rectangle : enable
uniform sampler2DRect texture0;
uniform sampler2DRect texture1;

varying vec3 vNormal;
varying vec3 vTangent;
varying vec3 vVertexToLight0;
varying vec3 vVertexToLight1;

void main()
{
  // get the color of the textures
  vec4 textureColor = texture2DRect(texture0, gl_TexCoord[0].st);
  vec3 normalmapItem = texture2DRect(texture1, gl_TexCoord[1].st).xyz * 2.0 - 1.0;

  // calculate matrix that transform from tangent space to normalmap space (contrary of intuition)
  vec3 binormal = cross(vNormal, vTangent);
  mat3 tangentSpace2normalmapSpaceMat = mat3(vTangent, binormal, vNormal);

  // disturb the normal
  vec3 disturbedNormal = tangentSpace2normalmapSpaceMat * normalmapItem;

  // calculate the diffuse term and clamping it to [0;1]
  float diffuseTerm0 = clamp(dot(disturbedNormal, vVertexToLight0), 0.0, 1.0);
  float diffuseTerm1 = clamp(dot(disturbedNormal, vVertexToLight1), 0.0, 1.0);

  vec3 irradiance = (diffuseTerm0 * gl_LightSource[0].diffuse.rgb + diffuseTerm1 * gl_LightSource[1].diffuse.rgb);

  // calculate the final color
  gl_FragColor = vec4(irradiance * textureColor.rgb, textureColor.w);
}
