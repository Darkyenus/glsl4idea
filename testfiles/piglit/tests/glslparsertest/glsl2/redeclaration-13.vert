// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - built-in exp is hidden by the structure */
struct exp {
    bvec4 bs;
    int x;
};

void main()
{
    gl_Position = vec4(exp(0.0));
}
