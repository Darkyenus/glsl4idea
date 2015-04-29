// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void main()
{
   const mat2 c1 = mat2(2.0);
   const mat2 c2 = mat2(0.5);
   const mat2 id = c1 * c2;
   gl_FragColor = vec4(0.0, id[0].x, 0.0, id[1].y);
}
