// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void testVoid (vec4 v, vec4 v1)
{
}

void testVoid (ivec4 v, ivec4 v1)
{
}

void main(void)
{
    vec4 v;
    ivec4 i;
    testVoid(i, i);
    testVoid(v, v);
    gl_FragColor = v;
}
