// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
#version 110
#extension GL_ARB_draw_buffers: disable

uniform vec4 a;

void main()
{
  gl_FragData[0] = a;
}
