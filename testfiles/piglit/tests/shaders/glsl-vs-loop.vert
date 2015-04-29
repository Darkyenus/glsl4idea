void main()
{
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

	int count = int(gl_Color.w);
	vec3 c = gl_Color.xyz;
	int i;

	for (i = 0; i < count; i++) {
		c = c.yzx;
	}

	gl_FrontColor = vec4(c, 1.0);
}

