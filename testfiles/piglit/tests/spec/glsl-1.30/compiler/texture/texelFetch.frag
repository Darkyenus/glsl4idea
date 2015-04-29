// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]

#version 130

uniform sampler2D a;
uniform ivec2 pos;
uniform int lod;

float f() {
      vec4 x;
      x = texelFetch(a, pos, lod);
      return x.x;
}
