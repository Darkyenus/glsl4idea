// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// check_link: true
// [end config]
//
// From the ARB_tessellation_shader spec:
//
//     There must be at least one layout qualifier specifying an output patch
//     vertex count in any program containing tessellation control shaders;

#version 150
#extension GL_ARB_tessellation_shader: require
//layout(vertices = 3) out;

void main() {
    gl_out[gl_InvocationID].gl_Position = vec4(0.0);
    gl_TessLevelOuter = float[4](1.0, 1.0, 1.0, 1.0);
    gl_TessLevelInner = float[2](1.0, 1.0);
}
