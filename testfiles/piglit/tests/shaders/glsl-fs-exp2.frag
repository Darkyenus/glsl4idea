uniform vec4 args1, args2;

void main()
{
	gl_FragColor = exp2(args1) + args2;
}
