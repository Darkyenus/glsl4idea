// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void test_function(const in int in_int, inout int out_int);
int test_function1(in int in_int, inout int out_int);

int test_function1(in int in_int1, inout int , out int);
int test_function1(in int in_int1, inout int , out int); 

uniform float array_float[2]; 

struct light1 
{
   float intensity;
   vec3 position;
   int test_int[2];
   struct nested
   {
      int a;
      float f; 
   } light2;
} lightVar;
light1 ll2;

varying struct light3 {
    float i;
} ;

attribute struct light4 {
    float i;
};

struct light5 {
    float i ;
    float a[2];
} light5_inst;

uniform light3 uniformLight3;

uniform struct light6 {  
    float i;
};
uniform light6 uniformLight6; 


struct light7 {
    struct {
        struct {
	    struct {
	        float f;
            } light10;
        } light9;
     } light8;
} ;


light3 struct_var = light3(5.0); 

// Attribtue variables can only be Global
attribute float flt_attrib;
attribute vec2 vec2_attrib;
attribute vec3 vec3_attrib;
attribute vec4 vec4_attrib; 
attribute mat2 mat2_attrib; 
attribute mat3 mat3_attrib; 
attribute mat4 mat4_attrib; 

uniform float flt_uniform; 
uniform vec3 uniform_vec3; 
uniform mat3 uniform_mat3; 

uniform sampler1D samp[];  
uniform sampler1D samp1;  

const struct light12 { 
    int a;
} uniform_struct = light12(2);

varying vec3 varying_vec3; 
varying vec2 varying_vec2;  
varying vec4 varying_vec4;  
varying mat4 varying_mat4;  
varying mat2 varying_mat2;  
varying mat3 varying_mat3;  
varying float varying_flt;  

float frequencies[2]; 

void test_function2(int func_int)
{
}

void test_function3(light3);
void test_function4(light5 ll20);
void test_function5(light1);
light6 test_function6(int a);

const float FloatConst1 = 3.0 * 8.0, floatConst2 = 4.0;
const bool BoolConst1 = true && true || false; 
const bool BoolConst2 = false || !false && false; 

void main(void)
{

    int test_int1 =  42; 

    struct structMain {
        float i;
    } testStruct;

    struct {    
        structMain a;
    } aStruct;

    testStruct.i = 5.0 ; 
    struct_var.i = 5.0;  

    structMain newStruct, newStruct1;
    testStruct = newStruct; 
    newStruct = newStruct1;  

    lightVar.light2.f = 1.1; 

    light1 ll1; 
    ll1.light2.a = 1;  

     const struct const_struct {
        float i;
    } const_struct_inst = const_struct(1.0); 

    ll1 = ll2; 
    ll1.light2 = ll2.light2; 
    ll1.light2 = ll1.light2; 
    ll1.light2.f = ll2.light2.f;
    ll1.light2.f = ll1.light2.f;

    lightVar = ll2;
    ll2 = lightVar;

    light5 ll10;

    light7 ll7[];
    structMain newStruct2[2];
    newStruct2[0].i = 1.1; 
    
    ll7[0].light8.light9.light10.f = 1.1;


    bool test_bool4 = false ; 

    bool test_bool5 = 1.2 > 3.0 ; 

    int test_int2 =  047; 
    int test_int4 =  0xa8;  // testing for hexadecimal numbers

    float test_float1 = 1.5; 
    float test_float2 = .01;  
    float test_float3 = 10.; 
    float test_float4 = 10.01; 
    float test_float5 = 23e+2; 
    float test_float6 = 23E-3; 
    float test_float8 = 23E2; 
    bool test_bool6 = BoolConst1 && ! (test_int1 != 0) && ! BoolConst1  && ! (FloatConst1 != 0.0) && (FloatConst1 != 0.0) && (test_float1 != 0.0); 

    vec4 color = vec4(0.0, 1.0, 0.0, 1.0); 
    vec4 color2 = vec4(0.0); 

    vec3 color4 = vec3(test_float8); 

    ivec4 test_int_vect1 = ivec4(1.0,1.0,1.0,1.0);  
    ivec3 test_int_vec3 = ivec3(1, 1, 1) ; 

    bvec4 test_bool_vect1 = bvec4(1., 1., 1. , 1. ); 

    vec2 test_vec2 = vec2(1., 1.); 
    vec2 test_vec3 = vec2(1., 1);  
    vec4 test_vec4 = vec4(test_int_vect1); 

    vec2 test_vec5 = vec2(color4);
    vec3 test_vec7 = vec3(color);   
    vec3 test_vec8 = vec3(test_vec2, test_float4);
    vec3 test_vec9 = vec3(test_float4, test_vec2);

    vec4 test_vec10 = vec4(test_vec9, 0.01); 
    vec4 test_vec11 = vec4(0.01, test_vec9); 

    vec4 test_vec12 = vec4(test_vec2, test_vec2); 

    mat2 test_mat2 = mat2(test_float3); 
    mat3 test_mat3 = mat3(test_float3); 
    mat4 test_mat4 = mat4(test_float3); 

    mat2 test_mat7 = mat2(test_vec2, test_vec2); 
    mat2 test_mat8 = mat2(01.01, 2.01, 3.01, 4.01); 

    mat3 test_mat9 = mat3(test_vec7, test_vec7, test_vec7); 
    mat4 test_mat10 = mat4(test_vec10, test_vec10, test_vec10, test_vec10); 
    test_mat10[1] = test_vec10; 
    

    mat2 test_mat12 = mat2(test_vec2, 0.01, 0.01); 
    mat2 test_mat13 = mat2(0.01, 5., test_vec2); 
    mat2 test_mat15 = mat2(0.1, 5., test_vec2 ); 

    //mat2 test_mat16 = mat2(test_mat9); 
    //mat2 test_mat17 = mat2(test_mat10); 

    float freq1[2]; 
    float freq2[];

    while(test_float1 < 1.0);

    float freq2[]; 
    float freq2[25]; 
    freq2[1] = 1.9 ; 
    const int array_index = 2;
    freq2[test_int1] = 1.9 ;
    freq2[array_index] = 1.8;
    
    const int const_int = 5; 
   
    test_float1 = varying_flt; 

    int out_int;
    int intArray[];
    test_function(test_int1, test_int1); 
    test_function(test_int1, intArray[2]); 

    vec3 vv = vec3(test_function1(test_int1, out_int));  
    bool bool_var = true;
    int test_int6 = int(bool_var); 
    test_float1 = float(bool_var); 
    test_float1 = float(test_int6); 
    test_int6 = int(test_float1); 
    bool_var = bool(test_int6); 
    bool_var = bool(test_float1); 
    test_float1 = float(test_vec9); 
    
    test_vec2.x = 1.2; 
    test_vec2.y = 1.4; 
    test_vec2.xy; 


    color.zy = test_vec2; 

   test_vec2[1] = 1.1;  
    
     test_mat2[0][0] = 1.1; 

    test_float1 += 1.0; 
    test_float1 -= 1.0;
    test_float1 *= 1.0;
    test_float1 /= 1.0;

    test_mat12 *= test_mat13 ; 
    test_mat12  *= test_float1;
    test_vec2 *= test_float1; 
    test_vec2 *= test_mat12; 
    test_float1++; 
    test_float1--; 
    --test_float1; 
    ++test_float1; 
    test_float1; 
    test_int1++; 
    test_int1--; 

    test_vec2 = test_vec2 + test_float1;   
    test_vec2 = test_float1 + test_vec2;   

    test_mat12 = test_mat12 * test_mat13; 
    test_vec2 = test_vec2 * test_vec5; 
 
    test_vec2++; 
    test_mat2++;

    bool test_bool2 = test_float2 > test_float3;  

    bool test_bool3 = test_int1 > test_int6 ; 

    test_bool3 = test_vec2 == test_vec5; 

    test_bool2 = test_bool3 && test_bool4; 
    test_bool2 = test_bool3 || test_bool4; 
    test_bool2 = test_bool3 ^^ test_bool4; 

    test_bool2 = !test_bool3;  

    test_bool3 = !(test_int1 > test_int6) ; 

    test_float1 = test_int1 > test_int6 ? test_float2 : test_float3;  
    test_vec2 = test_int1 > test_int6 ? test_vec2 : test_vec5;  
    if(test_bool2)  
        test_float1++;
    else
	test_float1--;

    if(test_float1 > test_float2)  
        test_float1++;

    if( test_bool2 )  
    {
        int if_int; 
        test_float1++;
    }

    if(test_bool2) 
       if(test_bool3)
           if(test_bool3)
	      test_float1++;

   for(int for_int=0; for_int < 5; for_int++) 
   {
       // do nothing as such
   }


   for(; test_bool2; ) 
   {
     int for_int;
   }

   for(; test_bool2 = (test_float1 > test_float2); ) 
   {
   }

   for(int for_int1; test_bool2; ) 
   {
     int for_int;
   }

   for(; test_bool2; test_float1++) 
   {
     int test_float;  
   }

   while(bool b = (test_float1 > test_float2)) 
   {
       break;
       continue;  
   }


   for(;;) { 
       for(;;)
       {
           break;
       }
   }


   while( test_bool2 )  
   {
       break;  
   }

   do {
       int dowhile_int;
       dowhile_int = 3;
   } while(test_bool2);  


    // VERTEX SHADER ONLY VARIABLES
    gl_PointSize = 4.0;  
    gl_ClipVertex = vec4(2.0, 3.0, 1.0, 1.1);  

    gl_Position = vec4(2.0, 3.0, 1.0, 1.1);
    gl_Position = gl_Vertex;


    // VERTEX SHADER BUILT-IN ATTRIBUTES

    vec4 builtInV4 = gl_Color + gl_SecondaryColor + gl_Vertex + gl_MultiTexCoord0 + gl_MultiTexCoord1 + gl_MultiTexCoord2 +  gl_MultiTexCoord3 +  gl_MultiTexCoord4 +  gl_MultiTexCoord5 +  gl_MultiTexCoord6 +  gl_MultiTexCoord7;
    

    int builtInI = gl_MaxLights + gl_MaxClipPlanes + gl_MaxTextureUnits + gl_MaxTextureCoords + gl_MaxVertexAttribs + gl_MaxVertexUniformComponents + gl_MaxVaryingFloats + gl_MaxVertexTextureImageUnits + gl_MaxCombinedTextureImageUnits + gl_MaxTextureImageUnits + gl_MaxFragmentUniformComponents + gl_MaxDrawBuffers ;
    

    mat4 builtInM4 = gl_ModelViewMatrix + gl_ModelViewProjectionMatrix + gl_ProjectionMatrix;

    gl_NormalMatrix;

    gl_TextureMatrix[gl_MaxTextureCoords-1];
    gl_TextureMatrix;
    gl_NormalScale ;

    gl_DepthRange.near ;

    test_float1 = gl_DepthRange.near; 
    test_float1 = gl_DepthRange.far; 
    test_float1 = gl_DepthRange.diff;

    gl_ClipPlane[gl_MaxClipPlanes-1] ;

    gl_Point.size; 
    gl_Point.sizeMin;
    gl_Point.sizeMax; 
    gl_Point.fadeThresholdSize ;
    gl_Point.distanceConstantAttenuation;
    gl_Point.distanceLinearAttenuation ;
    gl_Point.distanceQuadraticAttenuation;

    gl_MaterialParameters test; 
    gl_FrontMaterial.emission;

    color = gl_FrontMaterial.emission; 
    color = gl_FrontMaterial.ambient; 
    color = gl_FrontMaterial.diffuse;
    color = gl_FrontMaterial.specular;
    test_float1 = gl_FrontMaterial.shininess; 

    gl_LightSourceParameters lightSource;

    float builtInFloat1 = gl_LightSource[0].spotExponent;
    color = gl_LightSource[0].ambient; 
    color = lightSource.ambient; 
    color = lightSource.diffuse; 
    color = lightSource.specular; 
    color = lightSource.position; 
    color = lightSource.halfVector; 
    color4 = lightSource.spotDirection; 
    test_float1 = lightSource.spotExponent; 
    test_float1 = lightSource.spotCutoff; 
    test_float1 = lightSource.spotCosCutoff; 
    test_float1 = lightSource.constantAttenuation; 
    test_float1 = lightSource.linearAttenuation; 
    test_float1 = lightSource.quadraticAttenuation; 

    color = gl_LightModel.ambient;

    gl_LightModelParameters lightModel; 
    color = gl_LightModel.ambient; 
    color = lightModel.ambient; 

    color = gl_FrontLightModelProduct.sceneColor ;

    gl_LightModelProducts lightModelProd; 

    color = lightModelProd.sceneColor; 
    color = gl_FrontLightModelProduct.sceneColor; 

    color = gl_FrontLightProduct[0].ambient; 
    color = gl_FrontLightProduct[0].ambient; 
    gl_LightProducts lightProd;

    color =  lightProd.ambient; 
    color =  lightProd.diffuse;
    color =  lightProd.specular;


    color = gl_TextureEnvColor[gl_MaxTextureUnits-1];
    
    color = gl_EyePlaneS[gl_MaxTextureCoords-1] ;

    color = gl_EyePlaneT[gl_MaxTextureCoords-1]; 

    color = gl_EyePlaneR[gl_MaxTextureCoords-1];

    color = gl_EyePlaneQ[gl_MaxTextureCoords-1];

    color = gl_ObjectPlaneS[gl_MaxTextureCoords-1];

    color = gl_ObjectPlaneT[gl_MaxTextureCoords-1] ;

    color = gl_ObjectPlaneR[gl_MaxTextureCoords-1];

    color = gl_ObjectPlaneQ[gl_MaxTextureCoords-1]; 

    test_float1 = gl_Fog.density ;
    test_float1 = gl_Fog.start ;
    test_float1 = gl_Fog.end  ;
    test_float1 = gl_Fog.scale ;
    color = gl_Fog.color ;

    gl_FrontColor =  vec4(1.0, 1.0, 1.0, 1.0); 
    gl_BackColor =  vec4(1.0, 1.0, 1.0, 1.0);  
    gl_FrontSecondaryColor =  vec4(1.0, 1.0, 1.0, 1.0); 
    gl_BackSecondaryColor =  vec4(1.0, 1.0, 1.0, 1.0); 


    // VARYING VARIABLES AVAILABLE IN FRAGMENT AND VERTEX SHADERS BOTH
    gl_TexCoord[0] =  vec4(1.0, 1.0, 1.0, 1.0);  
    gl_FogFragCoord =  1.0;  

}

void test_function(const in int in_int, inout int out_int)
{
    out_int = 5; 
    int i = 5;
    return ;
}

int test_function1(in int in_int1, inout int unused1, out int unused2)
{
   float ff;
   in_int1 = 5;  
   return in_int1;
}

void test_function3(light3 ll)
{
    ll.i = 5.0;  
    varying_flt = 1.2;
}

void test_function4(light5 ll20)
{
    ll20.i = 10.0; 
}

void test_function5(light1 struct_light1)
{
    struct_light1.light2.a = 1; 
    light5 ll5;
    struct_light1.light2.f = ll5.i;
    struct_light1.light2.f++;
    struct_light1.light2.a++;
}

light6 test_function6(int a)  
{
    int x;
    light6 funcStruct;
    light7 funcStruct1;
    -x;
    x = x - x ; 
    mat2 m;
    m++;
    -m; 
    (m)++; 
    return funcStruct; 
}

float test_function7(light1 ll1, int light1 )  
{
    float f;
    
    struct ss1 {
        int a;
    };

    for(;;) {
        for(;;) {
            if(true)
                break;  
            if(true)
                break;  
            if(true)
                break;  
        }
        break;
    }

    for(;;);  
    return float(1);
}
