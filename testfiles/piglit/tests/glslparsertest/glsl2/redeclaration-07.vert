// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - like redeclaration-06.vert, but now the other comes first */
void main()
{
    struct foo {
       bvec4 bs;
    };
    float foo;

    gl_Position = vec4(0.0);
}
