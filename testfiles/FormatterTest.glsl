#version 330 core

// Some definitions
#if LIGHT_TYPE == 1
#define LIGHT_STRUCTNAME SpotLight
#else
#define LIGHT_STRUCTNAME PointLight
#endif

// Normal variables
in vec2 fragTexCoords;
out vec4 fragColor;

uniform int activeLightCount;

// Interface block
in VertexData
{
    vec3 color;
    vec2 texCoord;
} inData[];

layout(row_major) uniform MatrixBlock
{
    mat4 projection;
    layout(column_major) mat4 modelview;
} matrices[3];

// Blocks
void main() {
    func();
}

void main2()
{
    func();
}

vec3 addVectors(vec3 a, vec3 b) {
    return a + b;
}

void miscFunc(int t) {
    if (x == y) {
        int accum = 0;
        for (int i = 0;i < t; i++) {
            if (i == 2) {
                continue;
            }
            accum += 0;
        }
    } else {
        discard;
    }
}