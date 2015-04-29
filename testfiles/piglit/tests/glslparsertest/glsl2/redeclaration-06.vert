// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - structure type name conflicts with a variable name in same scope */
void main()
{
    float foo;
    struct foo {
       bvec4 bs;
    };

    gl_Position = vec4(0.0);
}
