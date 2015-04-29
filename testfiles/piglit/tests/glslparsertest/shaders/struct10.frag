// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

struct s {
    int i;
} s1[2];

void main()
{
   s1.i = 1;  // s1 is an array. s1[0].i is correct to use
}
