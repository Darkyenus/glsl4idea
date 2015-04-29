// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader_fp64
// [end config]
//
// GL_ARB_gpu_shader_fp64 spec states:
//
//     "Fragment outputs can only be float, single-precision
//      floating-point vectors, signed or unsigned integers or
//      integer vectors, or arrays of these."
//

#version 150
#extension GL_ARB_gpu_shader_fp64: require

out dvec4 color;

void main()
{
   color = dvec4(0.0, 0.0, 0.0, 0.0);
}

