// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

//test for else elif mismatch
#define test(x,y) (x+y)

void  main(void){
 int sum =0;
 #define x 8
 #endif
 #if (x==8)
   #undef x
 #endif
 
 #if 1
   #undef x
 #endif

 #if 1 
   #define t4 4
 #endif

 sum=test(3,6)+t4;
 #if 1
  #if 1
   #if 1
    #if 1
     #if 0
       #undef test
     #else
       #if 1
         #undef test
       #endif
       #if 0 
        #undef test
       #else
          #if 0
             #undef test
          #else
             #if 1 
                #undef test
             #else
               #undef test
             #else
               #jdhgj
             #endif
          #endif
       #endif
     #endif
    #endif
   #endif
  #endif
 #endif 
            
}
