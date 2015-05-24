// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

/* PASS */
#version 120
void main()
{
   const float a[] = float[](0.0, 1.0);
   const float b[2] = float[](4.0, 17.7);

   if (a != b)
      gl_FragColor = vec4(a[0], a[1], a[0], a[1]);
   else
      gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
