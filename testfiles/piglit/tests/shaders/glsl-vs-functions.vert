uniform vec4 args1;

/* In order to make the Mesa compiler not inline the function calls, have
 * return statements inside of branches.
 */
float f1(float f)
{
	if (f > 0.5)
		return 1.0;
	else
		return 0.0;
}

float f2(float f)
{
	if (f < 0.5)
		return 1.0;
	else
		return 0.0;
}

void main()
{
	gl_FrontColor = vec4(f1(args1.x),
			     f1(args1.y),
			     f2(args1.z),
			     f2(args1.w));
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}

