
class A {
    private val v = "err"

    // Inject GLSL language on this string
    @Language("GLSL")
    private val glsl = """
        uniform float $v;
        uniform float something;
        
        void main() {
            float foo;
            foo = something;// This should resolve
            foo = v;// This should not resolve (though it currently does)
            foo = err;// This should resolve (though it currently does not)
        }
    """
}