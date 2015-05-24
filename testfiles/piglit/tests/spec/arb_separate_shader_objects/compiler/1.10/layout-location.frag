// [config]
// expect_result: pass
// glsl_version: 1.10
// require_extensions: GL_ARB_separate_shader_objects
// [end config]
#version 110
#extension GL_ARB_separate_shader_objects: require

layout(location = 0) in vec4 a;

void main()
{
    gl_FragColor = a;
}
