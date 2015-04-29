// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

float radians(float f)
{
    return f; 
}

void main()
{
    float f = 45.0;
    f = radians(f);
}
