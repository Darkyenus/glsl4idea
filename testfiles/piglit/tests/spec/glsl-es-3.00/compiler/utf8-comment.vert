#version 300 es

/* [config]
 * expect_result: pass
 * glsl_version: 3.00
 * [end config]
 *
 * Page 8 (page 14 of the PDF) of the OpenGL ES Shading Language 3.00 spec
 * says:
 *
 *     "Inside comments, the character set is extended to allow any byte
 *     values to be used but with the exception that a byte with the value
 *     zero is always interpreted as the end of the string. The character
 *     encoding is assumed to be UTF-8 but no checking is performed for
 *     invalid characters."
 */

// This is all of the UTF8 HTML entities listed at
// http://en.wikipedia.org/wiki/List_of_XML_and_HTML_character_entity_references

// " & '
// < >
//   ¡ ¢ £ ¤ ¥ ¦ § ¨ © ª « ¬ ­ ® ¯
// ° ± ² ³ ´ µ ¶ · ¸ ¹ º » ¼ ½ ¾ ¿
// À Á Â Ã Ä Å Æ Ç È É Ê Ë Ì Í Î Ï
// Ð Ñ Ò Ó Ô Õ Ö × Ø Ù Ú Û Ü Ý Þ ß
// à á â ã ä å æ ç è é ê ë ì í î ï
// ð ñ ò ó ô õ ö ÷ ø ù ú û ü ý þ ÿ
// Œ œ
// Š š
// Ÿ
// ƒ
// ˆ ˜
// Α Β Γ Δ Ε Ζ Η Θ Ι Κ Λ Μ Ν Ξ Ο
// Π Ρ Σ Τ Υ Φ Χ Ψ Ω Ϊ Ϋ ά έ ή ί
// α β γ δ ε ζ η θ ι κ λ μ ν ξ ο
// π ρ ς σ τ υ φ χ ψ ω
// ϑ ϒ ϖ
// – — ‘ ’ ‚ “ ” „
// † ‡ • …
// ‰ ′ ″ ‹ › ‾
// ⁄
// €
// ℑ ℘ ℜ
// ™
// ℵ
// ← ↑ → ↓ ↔
// ↵
// ⇐ ⇑ ⇒ ⇓ ⇔
// ∀ ∂ ∃ ∅ ∇ ∈ ∉ ∋ ∏
// ∑ − ∗ √ ∝ ∞
// ∠ ∧ ∨ ∩ ∪ ∫
// ∼
// ≅ ≈
// ≠ ≡ ≤ ≥
// ⊂ ⊃ ⊄ ⊆ ⊇
// ⊕ ⊗
// ⊥
// ⋅
// ⌈ ⌉ ⌊ ⌋
// 〈 〉
// ◊
// ♠ ♣ ♥ ♦

void main()
{
  gl_Position = vec4(0.);
}
