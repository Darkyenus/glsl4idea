// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
uniform sampler2DRect s;
varying vec4 coord;

void main()
{
  gl_FragColor = texture2DRectProj(s, coord);
}
