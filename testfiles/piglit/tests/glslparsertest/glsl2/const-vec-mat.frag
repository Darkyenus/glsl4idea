// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void main()
{
   const vec3 c1 = vec3(1.0, 2.0, 3.0);
   const mat3 c2 = mat3(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
   const vec3 c3 = c1 * c2;
   const vec3 c4 = c2 * c1;
}
