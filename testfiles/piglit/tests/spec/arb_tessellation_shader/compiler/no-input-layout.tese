// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// check_link: true
// [end config]
//
// From the ARB_tessellation_shader spec:
//
//     At least one tessellation evaluation shader (compilation unit) in a
//     program must declare a primitive mode in its input layout;

#version 150
#extension GL_ARB_tessellation_shader: require
//layout(triangles, equal_spacing, ccw) in;

void main() { gl_Position = vec4(0.0);}
