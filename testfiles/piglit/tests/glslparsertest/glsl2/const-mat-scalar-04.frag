// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void main()
{
   const mat2 c1 = mat2(4.0, 4.0, 4.0, 4.0);
   const mat2 c2 = 2.0 / c1;
   const mat2 c3 = c2 / 0.5;
}
