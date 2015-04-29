// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

struct S2
{
        float f;
};

struct S1
{
        float f;
        S2 s2;
};

float process(S1 s1);
float process(S2 s2);

void main()
{
        S1 s1 = S1(1.0, S2(1.0));
        gl_Position = vec4(process(s1));
}

float process(S1 s1)
{
        return s1.f + process(s1.s2);
}

float process(S2 s2)
{
        return s2.f;
}
