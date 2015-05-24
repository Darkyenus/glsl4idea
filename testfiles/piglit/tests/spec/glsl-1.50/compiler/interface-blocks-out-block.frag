// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Tests that out interface blocks are rejected for the fragment shader.
//
// GLSLangSpec.1.50.11, 4.3.7 Interface Blocks:
// "It is illegal to have an input block in a vertex shader or an
//  output block in a fragment shader"

#version 150

out block {
    vec4 iface_var;
} inst;

void main()
{
}

