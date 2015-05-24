// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: false
// [end config]

#version 150

layout(points) in;
layout(points, max_vertices = 1) out;

flat in int a[];
noperspective in int b[];
smooth in int c[];

flat out int aa;
noperspective out int bb;
smooth out int cc;

void main()
{
	aa = a[0];
	bb = b[0];
	cc = c[0];
}
