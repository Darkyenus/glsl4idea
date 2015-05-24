// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

#version 120
void main()
{
   const vec4 t1 = vec4(-1.0, 0.0, -1.0, -1.0);
   const int one = 1;
   const vec4 c2 = one + t1;
   gl_FragColor = c2;
}
