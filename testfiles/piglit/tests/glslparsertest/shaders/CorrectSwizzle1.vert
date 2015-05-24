// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void main(void)
{
   vec4 v = vec4(1,2,3,4);
   vec3 v3 = vec3(5,6,7);
   vec4 v4  = vec4(normalize(v3.yzy).xyz.zyx, 1.0);
   gl_Position = v4 + vec4(normalize(gl_NormalMatrix * gl_Normal).xyz.zyx, v4.y);
}
