// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/*
 * bumpTBN_SH_FP.glsl - ambient, diffuse and specular lighting in tangent space
 * with shadow mapping and 4-samples PCF
 *
 * Simple test GLSL shader
 * Copyright (c) 2007 Cesare Tirabassi <norsetto@ubuntu.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

varying vec3 lvec, vvec;

uniform sampler2D colorMap;
uniform sampler2D bumpMap;
uniform sampler2DShadow shadowMap;

const vec2 texmapscale = vec2( 1.0/1024.0, 1.0/1024.0 );

vec4 offset_lookup(sampler2DShadow map, vec4 loc, vec2 offset )
{
	return shadow2DProj( map, vec4(loc.xy + offset * texmapscale * loc.w, loc.z, loc.w ) );
}

void main()
{
	vec3 lv = normalize(lvec);
	vec3 vv = normalize(vvec);
	vec3 nv = 2.0*texture2D(bumpMap, gl_TexCoord[0].st).rgb-1.0;

	float diff = max(dot(lv, nv), 0.0);

	float spec = 0.0;
	if (diff != 0.0)
		spec = pow( max( dot(-normalize(reflect(lv,nv)), vv), 0.0), 64.0 );

	vec4 texture = texture2D(colorMap, gl_TexCoord[0].st);

	vec2 offset = mod(gl_FragCoord.xy, 2.0);

	offset.y += offset.x;
	if ( offset.y > 1.1)
		offset.y = 0.0;

	vec4 shadow = (	offset_lookup( shadowMap, gl_TexCoord[1], offset+vec2(-1.5,  0.5) ) +
			offset_lookup( shadowMap, gl_TexCoord[1], offset+vec2( 0.5,  0.5) ) +
			offset_lookup( shadowMap, gl_TexCoord[1], offset+vec2(-1.5, -1.5) ) +
			offset_lookup( shadowMap, gl_TexCoord[1], offset+vec2( 0.5, -1.5) ) )*0.25;

//	vec4 shadow = shadow2DProj(shadowMap, gl_TexCoord[1]);

	gl_FragColor = texture*( shadow*( 0.1 + 0.75 * diff + spec * texture.a ) + 0.2 );
}
