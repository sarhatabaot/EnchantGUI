package me.tychsen.enchantgui.permissions;

import java.io.Serial;

/**
 * @author sarhatabaot
 */
public class TooManyEnchantmentsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public TooManyEnchantmentsException(final String message) {
        super(message);
    }
}
