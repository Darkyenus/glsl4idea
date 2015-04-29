// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_derivative_control
// [end config]

#version 150
#extension GL_ARB_derivative_control: require

#if !defined GL_ARB_derivative_control
#  error GL_ARB_derivative_control is not defined
#elif GL_ARB_derivative_control != 1
#  error GL_ARB_derivative_control is not equal to 1
#endif

/* Make sure that the functions are defined */
in vec4 val;
out vec4 color;
void main() {
  color = dFdx(val);
  color += dFdy(val);
  color += fwidth(val);
  color += dFdxCoarse(val);
  color += dFdyCoarse(val);
  color += fwidthCoarse(val);
  color += dFdxFine(val);
  color += dFdyFine(val);
  color += fwidthFine(val);
}
