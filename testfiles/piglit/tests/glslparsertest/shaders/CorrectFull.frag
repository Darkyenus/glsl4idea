// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void test_function(in int in_int, inout int out_int);
int test_function1(in int in_int, inout int out_int);

uniform float array_float[2]; 

struct light1 
{
    float intensity;
   vec3 position;
   int test_int[2];
   struct 
   {
      int a;
      float f; 
   } light2;
} lightVar;

struct light3 {
    int i;
} ;

uniform float flt_uniform;
uniform vec3 uniform_vec3;
uniform mat3 uniform_mat3;

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
    return;
}

const float FloatConst1 = 3.0 * 8.0, floatConst2 = 4.0; 
const bool BoolConst1 = true && true || false; 
const bool BoolConst2 = false || !false && false; 

void main(void)
{
 
    bool test_bool5 = 1.2 > 3.0 ; 

    int test_int1 =  42; 
    int test_int2 =  047; 
    int test_int4 =  0xa8;  // testing for hexadecimal numbers
    int test_int5 =  0xa8F; // testing for hexadecimal numbers

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

    ivec4 test_int_vect1 = ivec4(1.,1.,1.,1.); 
    bvec4 test_bool_vect1 = bvec4(1., 1., 1. , 1. ); 

    vec2 test_vec2 = vec2(1., 1.); 
    vec2 test_vec3 = vec2(1., 1); 

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
    
    mat2 test_mat12 = mat2(test_vec2, 0.01, 0.01); 
    mat2 test_mat13 = mat2(0.01, 5., test_vec2); 
    mat2 test_mat14 = mat2(1, 5., test_vec2); 
    mat2 test_mat15 = mat2(0.1, 5., test_vec2 ); 


    float freq1[2]; 
    float freq2[]; 

    const int const_int = 5;

    int out_int;
    test_function(test_int1, test_int1); 
    test_function(test_int1, out_int);
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

    color.rgba;

   test_vec2[0] = 1.1; 
    
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

    test_mat12 = test_mat12 * test_mat13; 
    test_vec2 = test_vec2 * test_vec5; 
 
    test_vec2++; 
    test_mat2++; 

    test_float1 = 1.0; 

    bool test_bool2 = test_float2 > test_float3;  
    test_bool2 = test_float2 > test_float3;  

    test_bool2 = test_int1 > test_int6 ; 
    bool test_bool3, test_bool4;
    
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

   while( test_bool2 )  
   {
       break;  
   }

   do {
       int dowhile_int;
       dowhile_int = 3;
   } while(test_bool2);  

   if(test_float1 < 2.2)
      discard; 

    // FRAGMENT SHADER ONLY VARIABLES
    gl_FragColor = vec4(2.0, 3.0, 1.0, 1.1); 

    color = gl_FragCoord ;
    gl_FragDepth = 1.0; 

    test_bool2 = gl_FrontFacing ;



    // VARYING VARIABLES AVAILABLE IN FRAGMENT AND VERTEX SHADERS BOTH
    color = gl_TexCoord[0]; 
    float builtInF = gl_FogFragCoord; 
}

void test_function(in int in_int, inout int out_int)
{
    out_int = in_int;  
    int i = 5;
}
varying float var_flt ; 

int test_function1(int in_int, inout int out_int)
{
   float ff;
   ff = var_flt; 
   return int(ff);
}
