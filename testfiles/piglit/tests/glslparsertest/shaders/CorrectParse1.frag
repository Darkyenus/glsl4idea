// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

uniform vec3 a[8];

uniform bool ub;
varying mat4 vm;

int foo(float);

float bar(int i)
{
    return float(i);
}

void main (void)
{
    const int x = 3;
    mat4 a[4]; 
    vec4 v;
    float f;

    for (f = 0.0; f != 3.3; ++f)
    {
    }

    vec3 v3[x + x];

    int vi = foo(2.3);

    vec3 v3_1 = v3[x];

    float f1 = a[x][2].z * float(x);  
    f1 = a[x][2][2] * float(x);
    f1 = v[2] * v[1];

    const int ci = 2;

}

int foo(float f)
{
    return 2;
}
