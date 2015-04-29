void main()
{
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

	int count = int(gl_Color.w);
	int count1 = int(count / 4);
	int count2 = count - count1 * 4;
	vec3 c = gl_Color.xyz;
	int i, j;

	for (i = 0; i < count1; i++) {
		for (j = 0; j < count2; j++) {
			c = c.yzx;
		}
	}

	gl_FrontColor = vec4(c, 1.0);
}

