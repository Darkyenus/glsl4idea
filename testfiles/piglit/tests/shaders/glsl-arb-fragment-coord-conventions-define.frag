#extension GL_ARB_fragment_coord_conventions : warn

void main()
{
#ifdef GL_ARB_fragment_coord_conventions
		gl_FragColor = vec4(0.0, 1.0, 0.0, 0.0);
#else
		gl_FragColor = vec4(1.0, 0.0, 0.0, 0.0);
#endif
}
