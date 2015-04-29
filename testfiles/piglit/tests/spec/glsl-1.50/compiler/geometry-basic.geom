// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: true
// [end config]

#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

void main()
{
  for (int i = 0; i < 3; i++) {
    gl_Position = vec4(0.0);
    EmitVertex();
  }
}
