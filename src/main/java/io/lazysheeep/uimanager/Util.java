package io.lazysheeep.uimanager;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

class Util
{
    static public int getTextComponentLength(TextComponent component)
    {
        return PlainTextComponentSerializer.plainText().serialize(component).length();
    }

    static public int getTextComponentWidth(TextComponent component)
    {
        float width = 0.0f;
        String str = PlainTextComponentSerializer.plainText().serialize(component);
        for(int i = 0; i < str.length(); i++)
        {
            int ascii = Character.codePointAt(str, i);

            if(ascii == '|')
                width += 0.25f;
            else if(ascii == '[' || ascii == ']')
                width += 0.5f;
            else if(ascii >= 0 && ascii <= 255)
                width += 1.0f;
            else
                width += 2.0f;
        }
        return (int)Math.ceil(width);
    }
}
