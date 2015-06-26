//Test for parsing qualifiers
//All should pass except when noted otherwise

uniform int number1;

varying centroid vec2 point;

//Layout test

layout(location = 3) in vec3 values[4];

layout(location = 0) out vec4 color;
layout(location = 1) out vec2 texCoord;
layout(location = 2, shared) out vec3 normal;

//Those should fail
layout location = 4) out vec4 color1;
layout(location = 5 out vec2 texCoord1;
layout(location = ) out vec3 normal1;
