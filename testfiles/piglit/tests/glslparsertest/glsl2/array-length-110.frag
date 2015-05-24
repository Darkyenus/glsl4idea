// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL: The length() method was added in GLSL 1.20. */
void main()
{
   float a[5];
   if (a.length() == 5)
      gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
   else
      gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
