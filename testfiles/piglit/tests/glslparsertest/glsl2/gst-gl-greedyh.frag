// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/*
 * GStreamer
 * Copyright (C) 2009 Julien Isorce <julien.isorce@mail.com>
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
uniform sampler2DRect tex_prev;
uniform float max_comb;
uniform float motion_threshold;
uniform float motion_sense;
uniform int width;
uniform int height;

void main () {
  vec2 texcoord = gl_TexCoord[0].xy;
  if (int(mod(texcoord.y, 2.0)) == 0)
    gl_FragColor = vec4(texture2DRect(tex_prev, texcoord).rgb, 1.0);
  else {
   // cannot have __ in a var so __ is replaced by _a
    vec2 texcoord_L1_a1, texcoord_L3_a1, texcoord_L1, texcoord_L3, texcoord_L1_1, texcoord_L3_1;
    vec3 L1_a1, L3_a1, L1, L3, L1_1, L3_1;

    texcoord_L1 = vec2(texcoord.x, texcoord.y - 1.0);
    texcoord_L3 = vec2(texcoord.x, texcoord.y + 1.0);
    L1 = texture2DRect(tex_prev, texcoord_L1).rgb;
    L3 = texture2DRect(tex_prev, texcoord_L3).rgb;
    if (int(ceil(texcoord.x)) == width && int(ceil(texcoord.y)) == height) {
      L1_1 = L1;
      L3_1 = L3;
    } else {
      texcoord_L1_1 = vec2(texcoord.x + 1.0, texcoord.y - 1.0);
      texcoord_L3_1 = vec2(texcoord.x + 1.0, texcoord.y + 1.0);
      L1_1 = texture2DRect(tex_prev, texcoord_L1_1).rgb;
      L3_1 = texture2DRect(tex_prev, texcoord_L3_1).rgb;
    }
    if (int(ceil(texcoord.x + texcoord.y)) == 0) {
      L1_a1 = L1;
      L3_a1 = L3;
    } else {
      texcoord_L1_a1 = vec2(texcoord.x - 1.0, texcoord.y - 1.0);
      texcoord_L3_a1 = vec2(texcoord.x - 1.0, texcoord.y + 1.0);
      L1_a1 = texture2DRect(tex_prev, texcoord_L1_a1).rgb;
      L3_a1 = texture2DRect(tex_prev, texcoord_L3_a1).rgb;
    }

   //STEP 1
    vec3 avg_a1 = (L1_a1 + L3_a1) / 2.0;
    vec3 avg = (L1 + L3) / 2.0;
    vec3 avg_1 = (L1_1 + L3_1) / 2.0;

    vec3 avg_s = (avg_a1 + avg_1) / 2.0;

    vec3 avg_sc = (avg_s + avg) / 2.0;

    vec3 L2 = texture2DRect(tex, texcoord).rgb;
    vec3 LP2 = texture2DRect(tex_prev, texcoord).rgb;

    vec3 best;

    if (abs(L2.r - avg_sc.r) < abs(LP2.r - avg_sc.r)) {
      best.r = L2.r;
    } else {
      best.r = LP2.r;
    }

    if (abs(L2.g - avg_sc.g) < abs(LP2.g - avg_sc.g)) {
      best.g = L2.g;
    } else {
      best.g = LP2.g;
    }

    if (abs(L2.b - avg_sc.b) < abs(LP2.b - avg_sc.b)) {
      best.b = L2.b;
    } else {
      best.b = LP2.b;
    }

   //STEP 2
    vec3 last;
    last.r = clamp(best.r, max(min(L1.r, L3.r) - max_comb, 0.0), min(max(L1.r, L3.r) + max_comb, 1.0));
    last.g = clamp(best.g, max(min(L1.g, L3.g) - max_comb, 0.0), min(max(L1.g, L3.g) + max_comb, 1.0));
    last.b = clamp(best.b, max(min(L1.b, L3.b) - max_comb, 0.0), min(max(L1.b, L3.b) + max_comb, 1.0));

   //STEP 3
    const vec3 luma = vec3 (0.299011, 0.586987, 0.114001);
    float mov = min(max(abs(dot(L2 - LP2, luma)) - motion_threshold, 0.0) * motion_sense, 1.0);
    last = last * (1.0 - mov) + avg_sc * mov;

    gl_FragColor = vec4(last, 1.0);
  }
}
