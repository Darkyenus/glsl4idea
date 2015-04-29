// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// From page 59 (page 65 of the PDF) of the GLSL 1.30 spec:
//
//     "The break jump can also be used only in loops and switch
//     statements. It is simply an immediate exit of the inner-most loop or
//     switch statements containing the break."

#version 130

void main() {
   int tmp = 0;
   switch (1) {
   case 1:
      while (tmp < 8) {
      	 if (tmp > 4)
	    break;
         tmp += 1;
      }
      break;	
   }

   gl_Position = vec4(0.0);
}
