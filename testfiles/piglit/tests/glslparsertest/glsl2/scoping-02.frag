// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
   if (true)
      float f = 0.3;
   else
      float g = 0.5;
   gl_FragColor = vec4(0.0, g, 0.0, 1.0);
}
