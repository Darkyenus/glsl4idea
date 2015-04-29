// [config]
// expect_result: pass
// glsl_version: 1.20
// require_extensions: GL_ARB_separate_shader_objects
// [end config]
#version 120
#extension GL_ARB_separate_shader_objects: require

layout(location = 0) out vec4 a;

void main()
{
    gl_Position = gl_Vertex;
    a = vec4(0);
}
