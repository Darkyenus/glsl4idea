// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void function(const in int i);  

void main()
{
    int i;
    function(i);  
}

// function definition has different parameter qualifiers than function declaration
void function(in int i)  
{  
   i = 3;
}
