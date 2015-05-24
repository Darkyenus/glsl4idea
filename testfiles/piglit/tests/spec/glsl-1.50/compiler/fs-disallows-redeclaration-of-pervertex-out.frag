// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]
//
// The gl_PerVertex output interface block only exists in vertex and
// geometry shaders.  Check that it may not be redeclared in fragment
// shaders.

#version 150

out gl_PerVertex {
  vec4 gl_Position;
};

void main()
{
}
