uniform vec4 color;

void main()
{
	int count = int(color.w);
	vec3 c = color.xyz;
	int i;

	for (i = 0; i < count; i++) {
		c = c.yzx;
	}

	gl_FragColor = vec4(c, 1.0);
}
