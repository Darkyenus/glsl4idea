varying vec4 red;
varying vec4 green;

void main()
{
	red = vec4(1, 0, 0, 0);
	green = vec4(0, 1, 0, 0);
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}

