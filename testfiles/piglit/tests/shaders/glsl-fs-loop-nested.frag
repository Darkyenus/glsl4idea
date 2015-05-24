uniform vec4 color;

void main()
{
	int count = int(color.w);
	int count1 = int(count / 4);
	int count2 = count - count1 * 4;
	vec3 c = color.xyz;
	int i, j;

	for (i = 0; i < count1; i++) {
		for (j = 0; j < count2; j++) {
			c = c.yzx;
		}
	}

	gl_FragColor = vec4(c, 1.0);
}
