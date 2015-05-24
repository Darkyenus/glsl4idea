// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

/* PASS */
#version 120
void main()
{
  const float a[] = float[](0.0, 1.0, 0.0, 1.0);

  gl_FragColor = vec4(a[0], a[1], a[2], a[3]);
}
