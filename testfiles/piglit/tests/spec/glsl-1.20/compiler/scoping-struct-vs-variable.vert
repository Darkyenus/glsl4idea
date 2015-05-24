// [config]
// expect_result: pass
// glsl_version: 1.20
// check_link: true
// [end config]
//
// GLSLangSpec.1.20.8, 4.2 Scoping:
//
// "If a nested scope redeclares a name used in an outer scope, it hides all
//  existing uses of that name. There is no way to access the hidden name or
//  make it unhidden, without exiting the scope that hid it."

#version 120

struct Name {
    float a;
};

void main()
{
    vec4 Name = vec4(1.0);
    gl_Position = Name;
}

