// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
uniform sampler2DRectShadow s;
varying vec4 coord;

void main()
{
  gl_FragColor = shadow2DRectProj(s, coord);
}
