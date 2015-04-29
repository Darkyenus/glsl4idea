// [config]
// expect_result: fail
// glsl_version: 1.10
// check_link: true
// [end config]

uniform sampler2D s[666];
uniform int i;

void main()
{
	gl_FragColor = texture2D(s[i], vec2(0.0));
}
