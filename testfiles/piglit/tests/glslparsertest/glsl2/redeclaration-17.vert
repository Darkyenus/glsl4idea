// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - id(5) should be the struct constructor, not the function */

int id(int x) {
   return x;
}

void main() {
   struct id {
      int n;
   };
   int i = id(5);
}
