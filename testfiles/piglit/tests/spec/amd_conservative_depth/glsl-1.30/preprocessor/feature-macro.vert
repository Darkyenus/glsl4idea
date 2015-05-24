// [config]
// expect_result: pass
// glsl_version: 1.30
// require_extensions: GL_AMD_conservative_depth
// [end config]
//
// From the AMD_conservative_depth spec:
//     A new preprocessor #define is added to the OpenGL Shading Language:
//        #define GL_AMD_conservative_depth 1

#version 130
#extension GL_AMD_conservative_depth: require

#if !defined GL_AMD_conservative_depth
#    error GL_AMD_conservative_depth is not defined
#elif GL_AMD_conservative_depth != 1
#    error GL_AMD_conservative_depth != 1
#endif

float foo() { return 0.0; }
