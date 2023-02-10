/*
 * ChatObject
 *
 * 0.0.1
 *
 * 09/02/2023
 */
package fr.enimaloc.kuiper.mojang;

import com.google.gson.annotations.Expose;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;

/**
 *
 */
public class ChatObject extends SimpleClassDescriptor {

    @Expose
    private String text;

    public ChatObject(String text) {
        this.text = text;
    }
}
