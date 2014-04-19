//
// Implementation dependent constants. The example values below
// are the minimum values allowed for these maximums.
//
const int gl_MaxLights = 8; // GL 1.0
const int gl_MaxClipPlanes = 6; // GL 1.0
const int gl_MaxTextureUnits = 2; // GL 1.3
const int gl_MaxTextureCoords = 2; // ARB_fragment_program
const int gl_MaxVertexAttribs = 16; // ARB_vertex_shader
const int gl_MaxVertexUniformComponents = 512; // ARB_vertex_shader
const int gl_MaxVaryingFloats = 32; // ARB_vertex_shader
const int gl_MaxVertexTextureImageUnits = 0; // ARB_vertex_shader
const int gl_MaxCombinedTextureImageUnits = 2; // ARB_vertex_shader
const int gl_MaxTextureImageUnits = 2; // ARB_fragment_shader
const int gl_MaxFragmentUniformComponents = 64;// ARB_fragment_shader
const int gl_MaxDrawBuffers = 1; // proposed ARB_draw_buffers

//
// Matrix state. p. 31, 32, 37, 39, 40.
//
uniform mat4 gl_ModelViewMatrix;
uniform mat4 gl_ProjectionMatrix;
uniform mat4 gl_ModelViewProjectionMatrix;
uniform mat4 gl_TextureMatrix[gl_MaxTextureCoords];
//
// Derived matrix state that provides inverse and transposed versions
// of the matrices above. Poorly conditioned matrices may result
// in unpredictable values in their inverse forms.
//
uniform mat3 gl_NormalMatrix; // transpose of the inverse of the upper leftmost 3x3 of gl_ModelViewMatrix
uniform mat4 gl_ModelViewMatrixInverse;
uniform mat4 gl_ProjectionMatrixInverse;
uniform mat4 gl_ModelViewProjectionMatrixInverse;
uniform mat4 gl_TextureMatrixInverse[gl_MaxTextureCoords];
uniform mat4 gl_ModelViewMatrixTranspose;
uniform mat4 gl_ProjectionMatrixTranspose;
uniform mat4 gl_ModelViewProjectionMatrixTranspose;
uniform mat4 gl_TextureMatrixTranspose[gl_MaxTextureCoords];
uniform mat4 gl_ModelViewMatrixInverseTranspose;
uniform mat4 gl_ProjectionMatrixInverseTranspose;
uniform mat4 gl_ModelViewProjectionMatrixInverseTranspose;
uniform mat4 gl_TextureMatrixInverseTranspose[gl_MaxTextureCoords];

//
// Normal scaling p. 39.
//
uniform float gl_NormalScale;

//
// Depth range in window coordinates, p. 33
//
struct gl_DepthRangeParameters {
    float near; // n
    float far; // f
    float diff; // f - n
};
uniform gl_DepthRangeParameters gl_DepthRange;
//
// Clip planes p. 42.
//
uniform vec4 gl_ClipPlane[gl_MaxClipPlanes];
//
// Point Size, p. 66, 67.
//
struct gl_PointParameters {
    float size;
    float sizeMin;
    float sizeMax;
    float fadeThresholdSize;
    float distanceConstantAttenuation;
    float distanceLinearAttenuation;
    float distanceQuadraticAttenuation;
};
uniform gl_PointParameters gl_Point;

//
// Material State p. 50, 55.
//
struct gl_MaterialParameters {
    vec4 emission; // Ecm
    vec4 ambient; // Acm
    vec4 diffuse; // Dcm
    vec4 specular; // Scm
    float shininess; // Srm
};
uniform gl_MaterialParameters gl_FrontMaterial;
uniform gl_MaterialParameters gl_BackMaterial;

//
// Light State p 50, 53, 55.
//
struct gl_LightSourceParameters {
    vec4 ambient; // Acli
    vec4 diffuse; // Dcli
    vec4 specular; // Scli
    vec4 position; // Ppli
    vec4 halfVector; // Derived: Hi
    vec3 spotDirection; // Sdli
    float spotExponent; // Srli
    float spotCutoff;           // Crli
                                // (range: [0.0,90.0], 180.0)
    float spotCosCutoff;        // Derived: cos(Crli)
                                // (range: [1.0,0.0],-1.0)
    float constantAttenuation; // K0
    float linearAttenuation; // K1
    float quadraticAttenuation;// K2
};
uniform gl_LightSourceParameters gl_LightSource[gl_MaxLights];
struct gl_LightModelParameters {
    vec4 ambient; // Acs
};
uniform gl_LightModelParameters gl_LightModel;
//
// Derived state from products of light and material.
//
struct gl_LightModelProducts {
    vec4 sceneColor; // Derived. Ecm + Acm * Acs
};
uniform gl_LightModelProducts gl_FrontLightModelProduct;
uniform gl_LightModelProducts gl_BackLightModelProduct;

struct gl_LightProducts {
    vec4 ambient; // Acm * Acli
    vec4 diffuse; // Dcm * Dcli
    vec4 specular; // Scm * Scli
};
uniform gl_LightProducts gl_FrontLightProduct[gl_MaxLights];
uniform gl_LightProducts gl_BackLightProduct[gl_MaxLights];

//
// Texture Environment and Generation, p. 152, p. 40-42.
//
uniform vec4 gl_TextureEnvColor[gl_MaxTextureUnits];
uniform vec4 gl_EyePlaneS[gl_MaxTextureCoords];
uniform vec4 gl_EyePlaneT[gl_MaxTextureCoords];
uniform vec4 gl_EyePlaneR[gl_MaxTextureCoords];
uniform vec4 gl_EyePlaneQ[gl_MaxTextureCoords];
uniform vec4 gl_ObjectPlaneS[gl_MaxTextureCoords];
uniform vec4 gl_ObjectPlaneT[gl_MaxTextureCoords];
uniform vec4 gl_ObjectPlaneR[gl_MaxTextureCoords];
uniform vec4 gl_ObjectPlaneQ[gl_MaxTextureCoords];

//
// Fog p. 161
//
struct gl_FogParameters {
    vec4 color;
    float density;
    float start;
    float end;
    float scale; // Derived: 1.0 / (end - start)
};
uniform gl_FogParameters gl_Fog;
