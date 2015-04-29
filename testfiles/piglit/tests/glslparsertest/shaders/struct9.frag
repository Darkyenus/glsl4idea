// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

const struct s {
    int i;
} s1 = s(1);

void main()
{
   s1.i = 1;  // const struct members cannot be modified
}
