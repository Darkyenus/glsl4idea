// [config]
// expect_result: pass
// glsl_version: 1.20
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

#version 120
/* The #version was added by anholt in converting to piglit due to the
 * use of the array constructor.
 */

#extension GL_ARB_texture_rectangle : enable
uniform sampler2DRect tex;
uniform float norm_const;
uniform float norm_offset;
uniform float kernel[9];
void main () {
  vec2 offset[9] = vec2[9] (
      vec2(-1.0,-1.0), vec2( 0.0,-1.0), vec2( 1.0,-1.0),
      vec2(-1.0, 0.0), vec2( 0.0, 0.0), vec2( 1.0, 0.0),
      vec2(-1.0, 1.0), vec2( 0.0, 1.0), vec2( 1.0, 1.0) );
  vec2 texturecoord = gl_TexCoord[0].st;
  int i;
  vec4 sum = vec4 (0.0);
  for (i = 0; i < 9; i++) {
    if (kernel[i] != 0.0) {
      vec4 neighbor = texture2DRect(tex, texturecoord + vec2(offset[i]));
      sum += neighbor * kernel[i]/norm_const;
    }
  }
  gl_FragColor = sum + norm_offset;
}
