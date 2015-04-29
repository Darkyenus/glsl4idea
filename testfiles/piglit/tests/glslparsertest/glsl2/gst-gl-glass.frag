// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/*
 * GStreamer
 * Copyright (C) 2008 Julien Isorce <julien.isorce@gmail.com>
 * Inspired from http://www.mdk.org.pl/2007/11/17/gl-colorspace-conversions
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
uniform float width, height;
void main () {
  float p = 0.0525;
  float L1 = p*width;
  float L2 = width - L1;
  float L3 = height - L1;
  float w = 1.0;
  float r = L1;
  if (gl_TexCoord[0].x < L1 && gl_TexCoord[0].y < L1)
      r = sqrt( (gl_TexCoord[0].x - L1) * (gl_TexCoord[0].x - L1) + (gl_TexCoord[0].y - L1) * (gl_TexCoord[0].y - L1) );
  else if (gl_TexCoord[0].x > L2 && gl_TexCoord[0].y < L1)
      r = sqrt( (gl_TexCoord[0].x - L2) * (gl_TexCoord[0].x - L2) + (gl_TexCoord[0].y - L1) * (gl_TexCoord[0].y - L1) );
  else if (gl_TexCoord[0].x > L2 && gl_TexCoord[0].y > L3)
      r = sqrt( (gl_TexCoord[0].x - L2) * (gl_TexCoord[0].x - L2) + (gl_TexCoord[0].y - L3) * (gl_TexCoord[0].y - L3) );
  else if (gl_TexCoord[0].x < L1 && gl_TexCoord[0].y > L3)
      r = sqrt( (gl_TexCoord[0].x - L1) * (gl_TexCoord[0].x - L1) + (gl_TexCoord[0].y - L3) * (gl_TexCoord[0].y - L3) );
  if (r > L1)
      w = 0.0;
  vec4 color = texture2DRect (tex, gl_TexCoord[0].st);
  gl_FragColor = vec4(color.rgb, gl_Color.a * w);
}
