void main()
{
	gl_Position = gl_Vertex;
	gl_TexCoord[0] = (gl_Vertex * gl_TextureMatrix[1] + 1.0) / 2.0;
}
