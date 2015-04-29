uniform sampler2DRect tex;

void main()
{
	gl_FragColor = texture2DRectProj(tex,
					 vec4(gl_TexCoord[0].xy, 0.0, 2.0));
}
