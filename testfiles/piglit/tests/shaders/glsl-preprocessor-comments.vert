#define YES 1

void main()
{
#if YES // definitely yes
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
#endif
}

