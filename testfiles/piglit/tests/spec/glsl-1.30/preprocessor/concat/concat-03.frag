// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Check that the preprocessor concatenation operator can be used within the
// definition of a preprocessor function.

#version 130

#define CONCAT(x, y, z) x ## y ## z

void main()
{
    CONCAT(i, n, t) x;
}
