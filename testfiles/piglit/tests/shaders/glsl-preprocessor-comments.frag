uniform vec4 args1;

#define YEAH 1

void main()
{
#if YEAH  // oh yeah
	gl_FragColor = args1;
#endif
}
