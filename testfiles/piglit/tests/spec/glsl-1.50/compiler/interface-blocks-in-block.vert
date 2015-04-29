// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Tests that in interface blocks are rejected for the vertex shader.
//
// GLSLangSpec.1.50.11, 4.3.7 Interface Blocks:
// "It is illegal to have an input block in a vertex shader or an
//  output block in a fragment shader"

#version 150

in block {
    vec4 iface_var;
} inst;

void main()
{
    gl_Position = vec4(0.0);
}

