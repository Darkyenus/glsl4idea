//
// GLSL fragment shader builtins (first in GLSL 110)
// see https://www.opengl.org/registry/doc/GLSLangSpec.4.50.pdf § 7.1
// see https://www.opengl.org/registry/doc/GLSLangSpec.Full.1.20.8.pdf § 7.6
//

#if __VERSION__ == 110
varying vec4  gl_FrontColor;
varying vec4  gl_BackColor;
varying vec4  gl_FrontSecondaryColor;
varying vec4  gl_BackSecondaryColor;
#elif __VERSION__ == 120
varying vec4  gl_Color;
varying vec4  gl_SecondaryColor;
#endif

#if __VERSION__ <= 120
varying vec4  gl_TexCoord[];
varying float gl_FogFragCoord;
#endif

in  vec4  gl_FragCoord;
in  bool  gl_FrontFacing;
out vec4  gl_FragColor; // deprecated in 130
out vec4  gl_FragData[gl_MaxDrawBuffers]; // deprecated in 130
out float gl_FragDepth;

#if __VERSION__ >= 130
in  float gl_ClipDistance[];
#endif

#if __VERSION__ >= 150
in  vec2  gl_PointCoord;
in  int   gl_PrimitiveID;
#endif

#if __VERSION__ >= 400
in  int   gl_SampleID;
in  vec2  gl_SamplePosition;
out int   gl_SampleMask[];
#endif

#if __VERSION__ >= 420
in  int   gl_SampleMaskIn[];
#endif

#if __VERSION__ >= 430
in  int   gl_Layer;
in  int   gl_ViewportIndex;
#endif

#if __VERSION__ >= 450
in  bool  gl_HelperInvocation;
in  float gl_CullDistance[];
#endif
