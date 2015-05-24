// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void main()
{
   const vec4 c1 = vec4(0.5, -0.5, 0.5, -0.5);
   const vec4 c2 = 1.0 - c1;
   const vec4 c3 = c2 - 0.5;
   gl_FragColor = c3;
}
