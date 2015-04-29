/* Used with glsl-getactiveuniform-count.  Expect 2 active uniforms.
 */
uniform vec4 a;

void main()
{
	gl_Position = ftransform() + a;
}