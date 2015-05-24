uniform bool red;

void main()
{
	if (red)
		gl_FrontColor = vec4(1.0, 0.0, 0.0, 0.0);
	else
		gl_FrontColor = vec4(0.0, 1.0, 0.0, 0.0);
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}

