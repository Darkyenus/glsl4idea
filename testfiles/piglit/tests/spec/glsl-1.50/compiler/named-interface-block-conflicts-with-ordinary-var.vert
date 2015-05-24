// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]
//
// This test checks that once a variable has been declared, the name
// of that variable can't be re-used as an instance name for a named
// interface block.

#version 150

out vec4 foo;

out block {
    vec4 a;
} foo;

void main()
{
}
