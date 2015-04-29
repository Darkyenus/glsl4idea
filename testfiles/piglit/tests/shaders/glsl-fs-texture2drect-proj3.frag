uniform sampler2DRect tex;

void main()
{
	gl_FragColor = texture2DRectProj(tex, vec3(gl_TexCoord[0].xy, 2.0));
}
