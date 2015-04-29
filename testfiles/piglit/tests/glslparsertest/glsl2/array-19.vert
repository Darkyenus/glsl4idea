// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
void func(in vec4 vertices[12])
{
    gl_Position = vertices[0];
}
