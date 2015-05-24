// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_separate_shader_objects
// [end config]
#version 150
#extension GL_ARB_separate_shader_objects: require

in vec4 piglit_vertex;
layout(location = 0) out vec4 a;

void main()
{
    gl_Position = piglit_vertex;
    a = vec4(0);
}
