// [config]
// expect_result: pass
// glsl_version: 1.50
// [end config]
//
// Tests that the invariant qualifier can be applied to a member of an out block.

#version 150

out block {
    vec4 x;
};

invariant x;

void main()
{
}
