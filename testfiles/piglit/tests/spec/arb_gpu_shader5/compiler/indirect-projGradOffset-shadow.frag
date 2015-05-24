// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]
#version 150
#extension GL_ARB_gpu_shader5: require

uniform int i;
uniform vec4 coord;
uniform sampler2DShadow s[5];

void main()
{
  float f = textureProjGradOffset(s[i], coord, coord.xy, coord.zw,
				  ivec2(-5, 3));
  gl_FragColor = vec4(0, f, 0, 1);
}
