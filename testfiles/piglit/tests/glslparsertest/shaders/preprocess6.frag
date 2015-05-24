// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

// operator precedence and some macro expansions.

#define test (1+2)
#define test1 (test*4)
#define test2 (test1/test)
#define test3 (-1+2*3/4%test)
#define test4 (test & test1 |test2)
#define test5 (!8+~4+4-6)
#define test6 (test1>>1)
#define test7 (test1<<1)
#define test8 (test2^6)
#define test9 (test4 || test5 && test1)
#define test10 (0)

void main(void)
{
 int sum =0;
 sum = test4;
 sum = test3*test2+test1-test;
 sum = test3/test6 + test4*test7 - test7 % test9;
 sum = test10*test5;
}

