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
  const float a0 = a[0];
  const float a1 = a[1];
  const float a2 = a[2];
  const float a3 = a[3];

  gl_FragColor = vec4(a0, a1, a2, a3);
}
