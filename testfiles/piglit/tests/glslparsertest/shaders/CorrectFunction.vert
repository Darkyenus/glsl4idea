// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

uniform sampler1D sampler;
uniform sampler2D sampler2;
uniform sampler3D sampler3;
uniform samplerCube samplerc;
uniform sampler1DShadow sampler1dshadow;
uniform sampler2DShadow sampler2dshadow;

void main(void)
{
    float float1;
    vec2 test_vec2;
    vec3 test_vec3;
    vec4 test_vec4;
    bvec2 test_bvec2;
    bvec3 test_bvec3;
    bvec4 test_bvec4;
    ivec2 test_ivec2;
    ivec3 test_ivec3;
    ivec4 test_ivec4;
    bool test_bool;

    float1 = radians(45.0); 
    test_vec2 = radians(test_vec2); 
    test_vec3 = radians(test_vec3); 
    test_vec4 = radians(test_vec4); 

    float1 = degrees(float1);
    test_vec2 =  degrees(test_vec2);
    test_vec3 =  degrees(test_vec3);
    test_vec4 =  degrees(test_vec4);

    float1 = sin(float1 );
    test_vec2 = sin(test_vec2);
    test_vec3 = sin(test_vec3);
    test_vec4 = sin(test_vec4);

    float1 = cos(float1);
    test_vec2 = cos(test_vec2);
    test_vec3 = cos(test_vec3);
    test_vec4 = cos(test_vec4);

    float1 = tan(float1);
    test_vec2 = tan(test_vec2);
    test_vec3 = tan(test_vec3);
    test_vec4 = tan(test_vec4);

    float1 = asin(float1);
    test_vec2 = asin(test_vec2);
    test_vec3 = asin(test_vec3);
    test_vec4 = asin(test_vec4);

    float1 = acos(float1);
    test_vec2 = acos(test_vec2);
    test_vec3 = acos(test_vec3);
    test_vec4 = acos(test_vec4);

    float1 = atan(float1, float1);
    test_vec2 = atan(test_vec2, test_vec2);
    test_vec3 = atan(test_vec3, test_vec3);
    test_vec4 = atan(test_vec4, test_vec4);
    
    float1 = atan(float1);
    test_vec2 = atan(test_vec2);
    test_vec3 = atan(test_vec3);
    test_vec4 = atan(test_vec4);

    float1 = pow(float1, float1);
    test_vec2 = pow(test_vec2, test_vec2);
    test_vec3 = pow(test_vec3, test_vec3);
    test_vec4 = pow(test_vec4, test_vec4);

    float1 = exp2(float1);
    test_vec2 = exp2(test_vec2);
    test_vec3 = exp2(test_vec3);
    test_vec4 = exp2(test_vec4);

    float1 = log2(float1);
    test_vec2 = log2(test_vec2);
    test_vec3 = log2(test_vec3);
    test_vec4 = log2(test_vec4);

    float1 = sqrt(float1);
    test_vec2 = sqrt(test_vec2);
    test_vec3 = sqrt(test_vec3);
    test_vec4 = sqrt(test_vec4);

    float1 = inversesqrt(float1);
    test_vec2 = inversesqrt(test_vec2);
    test_vec3 = inversesqrt(test_vec3);
    test_vec4 = inversesqrt(test_vec4);

    float1 = abs(float1);
    test_vec2 = abs(test_vec2);
    test_vec3 = abs(test_vec3);
    test_vec4 = abs(test_vec4);

    float1 = sign(float1);
    test_vec2 = sign(test_vec2);
    test_vec3 = sign(test_vec3);
    test_vec4 = sign(test_vec4);

    float1 = floor(float1);
    test_vec2 = floor(test_vec2);
    test_vec3 = floor(test_vec3);
    test_vec4 = floor(test_vec4);

    float1 = ceil(float1);
    test_vec2 = ceil(test_vec2);
    test_vec3 = ceil(test_vec3);
    test_vec4 = ceil(test_vec4);

    float1 = fract(float1);
    test_vec2 = fract(test_vec2);
    test_vec3 = fract(test_vec3);
    test_vec4 = fract(test_vec4);

    float1 = mod(float1, float1);
    test_vec2 = mod(test_vec2, float1);
    test_vec3 = mod(test_vec3, float1);
    test_vec4 = mod(test_vec4, float1);
    test_vec2 = mod(test_vec2, test_vec2);
    test_vec3 = mod(test_vec3, test_vec3);
    test_vec4 = mod(test_vec4, test_vec4);

    float1 = min(float1, float1);
    test_vec2 = min(test_vec2, float1);
    test_vec3 = min(test_vec3, float1);
    test_vec4 = min(test_vec4, float1);
    test_vec2 = min(test_vec2, test_vec2);
    test_vec3 = min(test_vec3, test_vec3);
    test_vec4 = min(test_vec4, test_vec4);

    float1 = max(float1, float1);
    test_vec2 = max(test_vec2, float1);
    test_vec3 = max(test_vec3, float1);
    test_vec4 = max(test_vec4, float1);
    test_vec2 = max(test_vec2, test_vec2);
    test_vec3 = max(test_vec3, test_vec3);
    test_vec4 = max(test_vec4, test_vec4);

    float1 = clamp(float1, float1, float1);
    test_vec2 = clamp(test_vec2, float1, float1);
    test_vec3 = clamp(test_vec3, float1, float1);
    test_vec4 = clamp(test_vec4, float1, float1);
    test_vec2 = clamp(test_vec2, test_vec2, test_vec2);
    test_vec3 = clamp(test_vec3, test_vec3, test_vec3);
    test_vec4 = clamp(test_vec4, test_vec4, test_vec4);

    float1 = mix(float1, float1, float1);
    test_vec2 = mix(test_vec2, test_vec2, float1);
    test_vec3 = mix(test_vec3, test_vec3, float1);
    test_vec4 = mix(test_vec4, test_vec4, float1);
    test_vec2 = mix(test_vec2, test_vec2, test_vec2);
    test_vec3 = mix(test_vec3, test_vec3, test_vec3);
    test_vec4 = mix(test_vec4, test_vec4, test_vec4);

    float1 = step(float1, float1);
    test_vec2 = step(test_vec2, test_vec2);
    test_vec3 = step(test_vec3, test_vec3);
    test_vec4 = step(test_vec4, test_vec4);
    test_vec2 = step(float1, test_vec2); 
    test_vec3 = step(float1, test_vec3); 
    test_vec4 = step(float1, test_vec4); 

    float1 = smoothstep(float1, float1, float1);
    test_vec2 = smoothstep(test_vec2, test_vec2, test_vec2);
    test_vec3 = smoothstep(test_vec3, test_vec3, test_vec3);
    test_vec4 = smoothstep(test_vec4, test_vec4, test_vec4);
    test_vec2 = smoothstep(float1, float1, test_vec2); 
    test_vec3 = smoothstep(float1, float1, test_vec3); 
    test_vec4 = smoothstep(float1, float1, test_vec4); 

    float1 =  length(float1);
    float1 =  length(test_vec2);
    float1 =  length(test_vec3);
    float1 =  length(test_vec4);

    float1 =  distance(float1, float1);
    float1 =  distance(test_vec2, test_vec2);
    float1 =  distance(test_vec3, test_vec3);
    float1 =  distance(test_vec4, test_vec4);

    float1 =  dot(float1, float1);
    float1 =  dot(test_vec2, test_vec2);
    float1 =  dot(test_vec3, test_vec3);
    float1 =  dot(test_vec4, test_vec4);

    test_vec3 =  cross(test_vec3, test_vec3);

    float1 = normalize(float1);
    test_vec2 = normalize(test_vec2);
    test_vec3 = normalize(test_vec3);
    test_vec4 = normalize(test_vec4);

    float1 = faceforward(float1, float1, float1);
    test_vec2 = faceforward(test_vec2, test_vec2, test_vec2);
    test_vec3 = faceforward(test_vec3, test_vec3, test_vec3);
    test_vec4 = faceforward(test_vec4, test_vec4, test_vec4);

    float1 = reflect(float1, float1);
    test_vec2 = reflect(test_vec2, test_vec2);
    test_vec3 = reflect(test_vec3, test_vec3);
    test_vec4 = reflect(test_vec4, test_vec4);

    test_bvec2 = lessThan(test_vec2, test_vec2);
    test_bvec3 = lessThan(test_vec3, test_vec3);
    test_bvec4 = lessThan(test_vec4, test_vec4);

    test_bvec2 = lessThan(test_ivec2, test_ivec2);
    test_bvec3 = lessThan(test_ivec3, test_ivec3);
    test_bvec4 = lessThan(test_ivec4, test_ivec4);

    test_bvec2 = lessThanEqual(test_vec2, test_vec2);
    test_bvec3 = lessThanEqual(test_vec3, test_vec3);
    test_bvec4 = lessThanEqual(test_vec4, test_vec4);

    test_bvec2 =  lessThanEqual(test_ivec2, test_ivec2);
    test_bvec3 =  lessThanEqual(test_ivec3, test_ivec3);
    test_bvec4 =  lessThanEqual(test_ivec4, test_ivec4);

    test_bvec2 =  greaterThan(test_vec2, test_vec2);
    test_bvec3 =  greaterThan(test_vec3, test_vec3);
    test_bvec4 =  greaterThan(test_vec4, test_vec4);

    test_bvec2 =  greaterThan(test_ivec2, test_ivec2);
    test_bvec3 =  greaterThan(test_ivec3, test_ivec3);
    test_bvec4 =  greaterThan(test_ivec4, test_ivec4);

    test_bvec2 =  greaterThanEqual(test_vec2, test_vec2);
    test_bvec3 =  greaterThanEqual(test_vec3, test_vec3);
    test_bvec4 =  greaterThanEqual(test_vec4, test_vec4);

    test_bvec2 =  greaterThanEqual(test_ivec2, test_ivec2);
    test_bvec3 =  greaterThanEqual(test_ivec3, test_ivec3);
    test_bvec4 =  greaterThanEqual(test_ivec4, test_ivec4);

    test_bvec2 =  equal(test_vec2, test_vec2);
    test_bvec3 =  equal(test_vec3, test_vec3);
    test_bvec4 =  equal(test_vec4, test_vec4);

    test_bvec2 =  equal(test_ivec2, test_ivec2);
    test_bvec3 =  equal(test_ivec3, test_ivec3);
    test_bvec4 =  equal(test_ivec4, test_ivec4);

    test_bvec2 =  equal(test_bvec2, test_bvec2);
    test_bvec3 =  equal(test_bvec3, test_bvec3);
    test_bvec4 =  equal(test_bvec4, test_bvec4);

    test_bool =  any(test_bvec2);
    test_bool =  any(test_bvec3);
    test_bool =  any(test_bvec4);

    test_bool =  all(test_bvec2);
    test_bool =  all(test_bvec3);
    test_bool =  all(test_bvec4);

    float1 =  noise1(float1);
    float1 =  noise1(test_vec2);
    float1 =  noise1(test_vec3);
    float1 =  noise1(test_vec4);

    test_vec2 =  noise2(float1);
    test_vec2 =  noise2(test_vec2);
    test_vec2 =  noise2(test_vec3);
    test_vec2 =  noise2(test_vec4);

    test_vec3 =  noise3(float1);
    test_vec3 =  noise3(test_vec2);
    test_vec3 =  noise3(test_vec3);
    test_vec3 =  noise3(test_vec4);

    test_vec4 =  noise4(float1);
    test_vec4 =  noise4(test_vec2);
    test_vec4 =  noise4(test_vec3);
    test_vec4 =  noise4(test_vec4);

    float bias, lod;

    test_vec4 = texture1D(sampler, float1);
    test_vec4 = texture1DProj(sampler, test_vec2);
    test_vec4 = texture1DProj(sampler, test_vec4 );
    test_vec4 = texture1DLod(sampler, float1 , lod );
    test_vec4 = texture1DProjLod(sampler, test_vec2 , lod );
    test_vec4 = texture1DProjLod(sampler, test_vec4 , lod );
    test_vec4 = texture2D(sampler2, test_vec2 );
    test_vec4 = texture2DProj(sampler2, test_vec3 );
    test_vec4 = texture2DProj(sampler2, test_vec4 );
    test_vec4 = texture2DLod(sampler2, test_vec2 , lod );
    test_vec4 = texture2DProjLod(sampler2, test_vec3 , lod );
    test_vec4 = texture2DProjLod(sampler2, test_vec4 , lod );
    test_vec4 = texture3D(sampler3, test_vec3 );
    test_vec4 = texture3DProj(sampler3, test_vec4 );
    test_vec4 = texture3DLod(sampler3, test_vec3 , lod );
    test_vec4 = texture3DProjLod(sampler3, test_vec4 , lod );
    test_vec4 = textureCube(samplerc, test_vec3 );
    test_vec4 = textureCubeLod(samplerc, test_vec3 , lod );
    test_vec4 = shadow1D(sampler1dshadow, test_vec3 );
    test_vec4 = shadow2D(sampler2dshadow, test_vec3 );
    test_vec4 = shadow1DProj(sampler1dshadow, test_vec4 );
    test_vec4 = shadow2DProj(sampler2dshadow, test_vec4 );
    test_vec4 = shadow1DLod(sampler1dshadow, test_vec3 , lod );
    test_vec4 = shadow2DLod(sampler2dshadow, test_vec3 , lod );
    test_vec4 = shadow1DProjLod(sampler1dshadow, test_vec4, lod);
    test_vec4 = shadow2DProjLod(sampler2dshadow, test_vec4,lod);



    test_vec4 = ftransform();


   gl_Position = gl_Vertex ;
}
