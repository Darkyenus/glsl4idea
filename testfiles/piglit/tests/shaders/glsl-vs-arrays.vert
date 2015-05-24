uniform int index1; /* 3 */
uniform int index2; /* 2 */

void main()
{
	vec4 temp[4];

	temp[3]= vec4(0.0, 1.0, 0.0, 0.0);
	temp[index2] = temp[index1];

	gl_FrontColor = temp[2];
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}

