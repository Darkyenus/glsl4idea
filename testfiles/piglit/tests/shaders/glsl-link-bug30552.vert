vec4 foo(const vec4 v)
{
	return vec4(v.x, v.y, 0.0, 0.0);
}

void main()
{
	int i;

	for (i = 0; i < 1; i++) {
		foo(vec4(0.0, 0.0, 0.0, 0.0));

		if (true) {
			break;
		};
	}

	gl_Position = gl_Vertex;
}
