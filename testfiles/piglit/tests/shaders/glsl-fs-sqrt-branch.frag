uniform vec4 args1, args2;

void main()
{
	if (args2.x != 0.0)
		gl_FragColor = sqrt(args1);
	else
		gl_FragColor = args2;
}
