// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

struct s {
    int i;
} s1;

void main()
{
   s1.i++;
   s1++;  // structure cannot be incremented
}
