// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]
//
// Check that an error is generated when trying
// to change the value of a shader input.

#version 150

in Block {
  vec4 invar;
};

void main()
{
  invar = vec4(1.0);
  gl_FragColor = invar;
}
