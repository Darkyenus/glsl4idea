// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    {
        int i = 1;
        i++;
    }
    i++;  // i is not declared in this scope
}
