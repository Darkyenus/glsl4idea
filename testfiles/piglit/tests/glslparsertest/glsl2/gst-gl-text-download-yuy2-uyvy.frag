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
uniform sampler2DRect tex;
void main(void) {
  float fx,fy,r,g,b,r2,g2,b2,y1,y2,u,v;
  fx = gl_TexCoord[0].x;
  fy = gl_TexCoord[0].y;
  r=texture2DRect(tex,vec2(fx*2.0,fy)).r;
  g=texture2DRect(tex,vec2(fx*2.0,fy)).g;
  b=texture2DRect(tex,vec2(fx*2.0,fy)).b;
  r2=texture2DRect(tex,vec2(fx*2.0+1.0,fy)).r;
  g2=texture2DRect(tex,vec2(fx*2.0+1.0,fy)).g;
  b2=texture2DRect(tex,vec2(fx*2.0+1.0,fy)).b;
  y1=0.299011*r + 0.586987*g + 0.114001*b;
  y2=0.299011*r2 + 0.586987*g2 + 0.114001*b2;
  u=-0.148246*r -0.29102*g + 0.439266*b;
  v=0.439271*r - 0.367833*g - 0.071438*b ;
  y1=0.858885*y1 + 0.0625;
  y2=0.858885*y2 + 0.0625;
  u=u + 0.5;
  v=v + 0.5;
  /* "%s" replaced with "y2,u,y1,v" in conversion to piglit (yuy2 version) */
  gl_FragColor=vec4(y2,u,y1,v);
}
