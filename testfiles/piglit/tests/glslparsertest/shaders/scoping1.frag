// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    bool b;
    if (b)
    {
        int i = 1;
        i++;
    }
    i++;  // i is not declared in this scope
}
