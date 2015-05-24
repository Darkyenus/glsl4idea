// [config]
// expect_result: pass
// glsl_version: 1.20
// [end config]
//
// ensure that invariant-qualifier can be used on fs inputs
#version 120

invariant varying vec4 x;

void main()
{
  gl_FragColor = x;
}
