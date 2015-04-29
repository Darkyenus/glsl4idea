// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */

uniform sampler2DRect s;

void main()
{
  gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
}
