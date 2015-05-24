// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

/* PASS */
#version 120
struct s { float f; };

void main()
{
    s t = s(1); // an implicit conversion should happen here
}
