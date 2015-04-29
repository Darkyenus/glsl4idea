// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: false
// [end config]
//
//Tests compiler/parser: output of geometry shader may be a struct

/*
* GLSLLangSpec.1.50.09 4.3.6 Outputs:
* Vertex and geometry output variables output per-vertex data and are declared
* using the out storage qualifier, the centroid out storage qualifier, or the
* deprecated varying storage qualifier. They can only be float, floating-point
* vectors, matrices, signed or unsigned integers or integer vectors, or arrays
* or structures of any these.
*/

#version 150

layout(triangles) in;
layout(triangle_strip) out;

in int a[];
in float b[];
in vec3 c[];
in mat4 d[];

out struct foo
{
	int a;
	float b;
	vec3 c;
	mat4 d;
} s;

void main()
{
	for (int i = 0; i < 3; i++) {
		s.a = a[i];
		s.b = b[i];
		s.c = c[i];
		s.d = d[i];

		gl_Position = vec4(
				s.a +
				s.b +
				s.c.x +
				s.d[0].x
				);
		EmitVertex();
	}
}
