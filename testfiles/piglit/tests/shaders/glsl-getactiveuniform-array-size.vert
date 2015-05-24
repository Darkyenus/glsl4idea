uniform vec4 color[50];
uniform vec4 unused[100];

void main()
{
	gl_Position = gl_Vertex;
	gl_FrontColor = color[24];
}
