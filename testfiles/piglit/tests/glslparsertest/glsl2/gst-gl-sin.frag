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
vec3 rgb2hsl (vec3 v)
{
/* TODO: check this algorythm */
  float MIN, MAX;
  float r, g, b;
  float h, l, s;
  float delta;
  h = 0.0; l = 0.0; s = 0.0;
  r = v.r; g = v.g; b = v.b;
  MIN = min (r, min (g, b));
  MAX = max (r, max (g, b));
  delta = MAX - MIN;
  l = (MAX + MIN) / 2.0;
  if ((MAX - MIN) < 0.0001) { h = 0.0; s = 0.0; }
  else {
    if (l <= 0.5) s = (MAX - MIN) / (MAX + MIN);
    else s = (MAX - MIN) / (2.0 - MAX - MIN);
    if (r == MAX) h = (g - b) / delta;
    else if (g == MAX) h = 2.0 + (b - r) / delta;
    else h = 4.0 + (r - g) / delta;
    h *= 60.0;
    if (h < 0.0) h += 360.0;
  }
  return vec3 (h, l, s);
}

void main () {
  vec3 HSL, RGB;
  vec4 color = texture2DRect (tex, vec2(gl_TexCoord[0].st));
  float luma = dot(color.rgb, vec3(0.2125, 0.7154, 0.0721));
  HSL = rgb2hsl (color.rgb);
/* move hls discontinuity away from the desired red zone so we can use
 * smoothstep.. to try: convert degrees in radiants, divide by 2 and
 * smoothstep cosine */
  HSL.x += 180.0;
  if ((HSL.x) > 360.0) HSL.x -= 360.0;
/* damn, it is extremely hard to get rid of human face reds! */
/* picked hue is slightly shifted towards violet to prevent this but
 * still fails.. maybe hsl is not well suited for this */
  float a = smoothstep (110.0, 150.0, HSL.x);
  float b = smoothstep (170.0, 210.0, HSL.x);
  float alpha = a - b;
  gl_FragColor = color * alpha + luma * (1.0 - alpha);
}
