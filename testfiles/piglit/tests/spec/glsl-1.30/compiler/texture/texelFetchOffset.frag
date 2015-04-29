// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]

#version 130

uniform sampler2D a;
uniform ivec2 pos;
uniform ivec2 offset;
uniform int lod;

float f() {
      vec4 x;
      x = texelFetchOffset(a, pos, lod, offset);
      return x.x;
}
