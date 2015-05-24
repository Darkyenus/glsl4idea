uniform int index; /* 3 */

void main()
{
	vec4 temp[4];
	vec4 temp1;

	temp[3]= vec4(0.0, 1.0, 0.0, 0.0);

	temp1 = temp[index];

	gl_FrontColor = temp1;
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}

