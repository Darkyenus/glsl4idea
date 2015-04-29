//Annotator test file. Annotations should be where noted, nowhere else.

void BinaryOperatorTypeAnnotationTest() {
    float bota;
    bota = vec2(0.0); //Should warn here
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
