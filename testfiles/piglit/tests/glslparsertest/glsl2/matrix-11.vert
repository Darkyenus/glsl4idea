// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */

uniform mat4 a;

void main()
{
    gl_Position = a[1];
}
