//Annotator test file. Annotations should be where noted, nowhere else.

void BinaryOperatorTypeAnnotationTest() {
    float bota;
    bota = vec2(0.0); //Should warn here
}

void DeclarationAssignmentTypeAnnotationTest(){
    float foobaz = vec2(4.0); //Should warn here
}

vec2 CheckReturnTypeAnnotation(){
    return 0.0; //Should warn here
}

void ConditionCheckAnnotation(){
    if(42){}//Should warn here
}

void LValueAnnotator(){
    45 = 42;//Should warn here
}

struct MCA {
    int foo;
};
void MemberCheckAnnotation(){
    MCA bar;
    bar.fuzz = 67; //Should warn here
    bar.foo = 1;
}

int MissingReturnAnnotation1(){} //Should warn here
int MissingReturnAnnotation2(){
    return 1;
}
int MissingReturnAnnotation3(){
    bool condition;
    if(condition)return 0;
} //Should warn here
int MissingReturnAnnotation4(){
    bool condition;
    if(condition)return 0;
    else return 1;
    int x = 0;
}

void VectorComponentsAnnotation(){
    vec3 bar;
    bar.xr = vec2(0.0);//Should warn here (mixing)
    bar.rgba = vec4(0.0);//Should warn here (range)
    bar.xzy = vec3(0.0);
    bar.oo = vec2(0.0);//Should warn here (none of possible ranges)
}

void ConstructorParamCountAnnotation(){
    vec3 v3;
    vec4 v4;

    v3 = vec3();//Should warn here
    v3 = vec3(1.0);
    v3 = vec3(1.0, 2, 3.0);
    v3 = vec3(0, 0);//Should warn here
    v3 = vec3(0, 0, 0, 0);//Should warn here

    v3 = vec3(vec2(0.0), 0.0);
    v4 = vec4(0.0, vec2(0.0), 0.0);
    v4 = vec4(vec2(0.0), 0.0);//Should warn here

    v3 = vec3(vec4(1));
    v4 = vec4(1, vec4(1));
    v3 = vec3(vec3(1), 1);//Should warn here

    v3 = vec3(1.0, 1.0, someParameterWithUnknownType);

    //Examples from GLSL specification 4.50 5.4.2
    //All should be valid

    float f;
    int i;
    vec4 v4;
    ivec3 iv3;
    bvec4 bv4;

    v3 = vec3(f);
    v2 = vec2(f,f);
    iv3 = ivec3(i, i, i);
    bv4 = bvec4(i, i, f, f);
    v2 = vec2(v3);
    v3 = vec3(v4);
    v3 = vec3(v2, f);
    v3 = vec3(f, v2);
    v4 = vec4(v3, f);
    v4 = vec4(f, v3);
    v4 = vec4(v2,v2);

    mat2 m2;
    mat3 m3;
    mat4 m4;
    dvec2 dv2;
    dvec3 dv3;
    dvec4 dv4;

    m2 = mat2(f);
    m3 = mat3(f);
    m4 = mat4(f);

    m2 = mat2(v2, v2);
    m3 = mat3(v3, v3, v3);
    m4 = mat4(v4, v4, v4, v4);
    mat3x2 m32 = mat3x2(v2, v2, v2);
    dmat2 dm2 = dmat2(dv2, dv2);
    dmat3 dm3 = dmat3(dv3, dv3, dv3);
    dmat4 dm4 = dmat4(dv4, dv4, dv4, dv4);
    m2 = mat2(f, f, f, f);
    m3 = mat3(f, f, f, f, f, f, f, f, f);
    m4 = mat4(f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f);
    mat2x3 m23 = mat2x3(v2, f, v2, f);
    dmat2x4 dm24 = dmat2x4(dv3, d, d, dv3);

    mat4x4 m44;
    mat4x2 m42;
    mat3x3 m33;
    mat2x3 m23;

    m33 = mat3x3(m44);
    m23 = mat2x3(m42);
    m44 = mat4x4(m33);
}

//Unreachable Annotation
void UA1(){
    int x;
    return;
    int y; //Unreachable
    return; //Unreachable
}

void UA2(){
    bool condition;
    if(condition){
        return;
    }
    int x;
    return;
    int y; //Unreachable
}

void UA3(){
    bool condition;
    if(condition) return;
    else return;
    int x; //Unreachable
}
