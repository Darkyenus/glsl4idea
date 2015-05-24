/* Used with glsl-getactiveuniform-count.  Expect 2 active uniforms.
 */
uniform vec4 color[4];

void main()
{
	gl_Position = gl_ModelViewProjectionMatrixTranspose * gl_Vertex;
	gl_FrontColor = color[1];
}
