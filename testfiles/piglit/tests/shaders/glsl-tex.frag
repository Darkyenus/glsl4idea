uniform sampler2D sampler;

void main()
{
	gl_FragColor = texture2D(sampler, gl_TexCoord[0].xy);
}
