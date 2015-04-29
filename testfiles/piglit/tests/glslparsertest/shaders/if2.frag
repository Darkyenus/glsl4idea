// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    vec2 v;
    int i;
    if (v) // vectors cannot be used as conditional expression for if statement
        i++;
}
