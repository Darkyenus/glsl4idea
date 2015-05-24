// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/*
 * GStreamer
 * Copyright (C) 2008 Filippo Argiolas <filippo.argiolas@gmail.com>
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
uniform sampler2DRect tex;
uniform float width;
uniform float height;
void main () {
  vec2 tex_size = vec2 (width, height);
  vec2 texturecoord = gl_TexCoord[0].xy;
  vec2 normcoord;
  normcoord = texturecoord / tex_size - 1.0;
  float r = length (normcoord);
  normcoord *= 1.0 + smoothstep(0.25, 0.5, abs(normcoord));
  normcoord /= 2.0; /* zoom amount */
  texturecoord = (normcoord + 1.0) * tex_size;
  vec4 color = texture2DRect (tex, texturecoord);
  gl_FragColor = color * gl_Color;
}
