// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/*
 * GStreamer
 * Copyright (C) 2007 David A. Schleef <ds@schleef.org>
 * Copyright (C) 2008 Julien Isorce <julien.isorce@gmail.com>
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
uniform sampler2DRect Ytex, UVtex;
void main(void) {
  float fx, fy, y, u, v, r, g, b;
  fx = gl_TexCoord[0].x;
  fy = gl_TexCoord[0].y;
  /* %c replaced with 'r' in conversion to piglit -- YUY2 version */
  y = texture2DRect(Ytex,vec2(fx,fy)).r;
  /* %c replaced with 'g' in conversion to piglit -- YUY2 version */
  u = texture2DRect(UVtex,vec2(fx*0.5,fy)).g;
  /* %c replaced with 'b' in conversion to piglit -- YUY2 version */
  v = texture2DRect(UVtex,vec2(fx*0.5,fy)).b;
  y=1.164*(y-0.0627);
  u=u-0.5;
  v=v-0.5;
  r = y+1.5958*v;
  g = y-0.39173*u-0.81290*v;
  b = y+2.017*u;
  gl_FragColor = vec4(r, g, b, 1.0);
}
