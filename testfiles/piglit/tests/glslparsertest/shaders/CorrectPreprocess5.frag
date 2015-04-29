// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

//mutiple line macros - test case.

#define test 5
#define t1 1
#define t2 2
#define token (t1+t2)
#define test1 int sum =1;\
              sum = test;\
              sum = test+test;

#define test2 do{\
              test1\
              sum = sum +token;\
              sum = t2*t1;\
              }while(sum>0)

void main(void)
{
 int test3=1;
 test1
 test2;
 test3 = test;
 sum = test3;
} 
 
 
