// [config]
// expect_result: pass
// glsl_version: 1.30
// require_extensions: GL_AMD_conservative_depth
// [end config]
//
// From the AMD_conservative_depth spec:
//      Redeclarations are performed as follows:
//
//          out float gl_FragDepth;                             // Redeclaration that changes nothing is allowed
//
//          layout (depth_any) out float gl_FragDepth;          // Assume that gl_FragDepth may be modified in any way
//          layout (depth_greater) out float gl_FragDepth;      // Assume that gl_FragDepth may be modified such that its value will only increase
//          layout (depth_less) out float gl_FragDepth;         // Assume that gl_FragDepth may be modified such that its value will only decrease
//          layout (depth_unchanged) out float gl_FragDepth;    // Assume that gl_FragDepth will not be modified

#version 130
#extension GL_AMD_conservative_depth: require

layout (depth_less) out float gl_FragDepth;

float f() {
    return 0.0;
}
