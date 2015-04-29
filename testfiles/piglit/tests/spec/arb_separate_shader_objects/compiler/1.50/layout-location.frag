// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_separate_shader_objects
// [end config]
#version 150
#extension GL_ARB_separate_shader_objects: require

out vec4 fragcolor;
layout(location = 0) in vec4 a;

void main()
{
    fragcolor = a;
}
