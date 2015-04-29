// [config]
// expect_result: pass
// glsl_version: 1.20
// [end config]
//
// ensure that invariant-qualifier can be used on vs outputs
#version 120

invariant varying vec4 x;

void main()
{
  x = vec4(0,0,0,1);
  gl_Position = vec4(0,0,0,1);
}
