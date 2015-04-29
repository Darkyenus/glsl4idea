// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]

#version 130

uniform sampler2D a;
int i() {
      ivec2 x;

      x = textureSize(a, 0);
      return x.x;
}
