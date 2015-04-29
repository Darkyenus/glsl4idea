struct s {
	vec4 v[1];
	float f;
};
s a[1];

void main()
{
	a[0].v[0] = vec4(0.0, 0.0, 0.0, 0.0);
	a[0].f = 1.0;
	gl_FragColor = vec4(0.0, a[0].f, 0.0 ,1.0);
}
