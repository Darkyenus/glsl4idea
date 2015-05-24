// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_separate_shader_objects
// [end config]
#version 150
#extension GL_ARB_separate_shader_objects: require

layout(points) in;
layout(points) out;

layout(location = 0) in vec4 in_position;

layout(location = 0) out vec4 position;
layout(location = 1) out vec4 color;

void main()
{
    position = in_position;
    color = vec4(0);
    EmitVertex();
}
