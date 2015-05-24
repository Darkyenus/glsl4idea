// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - variable redeclared */
void main()
{
    float foo;
    float foo;
    gl_Position = vec4(0.0);
}

