/* Note that we don't use the red varying */
varying vec4 green;
uniform int do_red;

void main()
{
	if (do_red != 0)
		gl_FragColor = vec4(1,0,0,0);
	else
		gl_FragColor = green;
}
