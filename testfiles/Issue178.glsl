#version 120

#define FOO
#define BAR FOO

#define ASSERT(condition)

#define ASSERT_EXTRA(condition) while (true) { ASSERT(condition); int something = 0; }

void main() {
    BAR float foobar = FOO BAR + FOO 1 BAR;

    ASSERT(foobar == 1);

    ASSERT_EXTRA(foobar == 1)
}
