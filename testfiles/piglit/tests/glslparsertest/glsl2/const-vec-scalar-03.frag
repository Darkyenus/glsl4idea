// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void main()
{
   const vec4 c1 = vec4(0.0, 1.0, 0.0, 1.0);
   const vec4 c2 = 2.0 * c1;
   const vec4 c3 = c2 * 0.5;
   gl_FragColor = c3;
}
