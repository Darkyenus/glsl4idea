/*
 * Copyright (c) 2009 Nick Bowler
 * Copyright © 2010 Intel Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

uniform vec4 args;
uniform sampler2D sampler;

void main()
{
	vec4 secondary = texture2D(sampler, gl_TexCoord[0].st);
	vec3 colour    = args.rgb;

	/* Removing the "if useSecondary" here (but keeping the
	 * multiplication) causes the shader to work.
	 * The failure does not depend on the value assigned to useSecondary.
	 *
	 * Making colour vec4 and doing vec4 multiplication also causes the
	 * shader to work.
	 */
	if (args.a != 0.0) {
		colour *= secondary.rgb;
	}

	gl_FragColor = vec4(colour, 1);
}
