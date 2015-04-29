/* Shared with glsl-getactiveuniform-length, glsl-getactiveuniform-count */
uniform vec4 color[4];
uniform vec4 totally_unused;

void main()
{
	gl_Position = gl_Vertex;
	gl_FrontColor = color[1];
}
