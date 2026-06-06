#version 430

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoords;
layout (location = 4) in mat4 modelMatrix;

out vec2 TexCoords;
out vec3 FragPos;
out vec3 Normal;
out vec4 FragPosLightSpace;

layout (std140, binding = 0) uniform Matrices
{
    mat4 projection;
    mat4 view;
};

// It crashed when I typed the ; in here
uniform mat4 lightSpaceMatrix;

void main()
{
    vec3 fragPos = vec3(modelMatrix * vec4(aPos, 1.0));
    FragPos = fragPos;
    Normal = mat3(transpose(inverse(modelMatrix))) * aNormal;
    TexCoords = aTexCoords;
    FragPosLightSpace = lightSpaceMatrix * vec4(fragPos, 1.0);

    gl_Position = projection * view *  vec4(fragPos, 1.0);
}