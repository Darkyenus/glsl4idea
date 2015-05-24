//This file should parse without errors

#version 330

#define INTERFAC buffer ShaderStorageBlock { mat4 projection; };

INTERFAC
// /\ should be hightlighted as replaced

#define INTER buffer ShaderStorageBlock {
#define FACE  mat4 projection; };

INTER FACE
//\ and /\ should get highlighted as replaced

#define INTER1 buffer ShaderStorageBlock
#define FACE1  { mat4 projection; };

INTER1 FACE1
//\ and /\ should get highlighted as replaced

#define LOWP lowp

LOWP float boo;

#define FOO 3

#define BAR FOO + 4

int foobar = BAR;

#define BAZ FOO + BAR * BAR + FOO * 6

int baz = BAZ;

#undef BAR

int baaz = BAZ;

#define Min(x,y) (x) < (y) ? (x):(y)

void main(){
	int i = Min(x,y);

}