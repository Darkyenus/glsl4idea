#version 120
uniform sampler2D tex;

void main()
{
	gl_FragColor = vec4(gl_PointCoord.xy * 1.1 - 0.05, 0, 0);
}
