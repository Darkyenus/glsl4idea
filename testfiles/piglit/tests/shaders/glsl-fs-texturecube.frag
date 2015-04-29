uniform samplerCube tex;

void main()
{
	gl_FragColor = textureCube(tex, gl_TexCoord[0].xyz);
}
