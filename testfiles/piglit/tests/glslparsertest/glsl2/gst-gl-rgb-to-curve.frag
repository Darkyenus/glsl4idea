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
uniform sampler1D curve;
void main () {
  vec2 texturecoord = gl_TexCoord[0].st;
  vec4 color = texture2DRect (tex, texturecoord);
  vec4 outcolor;
  outcolor.r = texture1D(curve, color.r).r;
  outcolor.g = texture1D(curve, color.g).g;
  outcolor.b = texture1D(curve, color.b).b;
  outcolor.a = color.a;
  gl_FragColor = outcolor;
}
