// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS - built-in exp is outside the global scope */
struct exp {
    bvec4 bs;
};

void main()
{
    gl_Position = vec4(0.0);
}
