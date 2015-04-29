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
uniform float w, h;
void main(void) {
  float r,g,b,r2,b2,g2,y,u,v;
  vec2 nxy=gl_TexCoord[0].xy;
  vec2 nxy2=mod(2.0*nxy, vec2(w, h));
  r=texture2DRect(tex,nxy).r;
  g=texture2DRect(tex,nxy).g;
  b=texture2DRect(tex,nxy).b;
  r2=texture2DRect(tex,nxy2).r;
  g2=texture2DRect(tex,nxy2).g;
  b2=texture2DRect(tex,nxy2).b;
  y=0.299011*r + 0.586987*g + 0.114001*b;
  u=-0.148246*r2 -0.29102*g2 + 0.439266*b2;
  v=0.439271*r2 - 0.367833*g2 - 0.071438*b2 ;
  y=0.858885*y + 0.0625;
  u=u + 0.5;
  v=v + 0.5;
  gl_FragData[0] = vec4(y, 0.0, 0.0, 1.0);
  gl_FragData[1] = vec4(u, 0.0, 0.0, 1.0);
  gl_FragData[2] = vec4(v, 0.0, 0.0, 1.0);
}
